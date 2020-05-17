<?php

namespace MobileApiBundle\Controller;

use AppBundle\Entity\User;
use ForumBundle\Entity\Post;
use ForumBundle\Entity\Topic;
use Symfony\Bundle\FrameworkBundle\Controller\Controller;
use Symfony\Component\HttpFoundation\JsonResponse;
use Symfony\Component\Serializer\Normalizer\ObjectNormalizer;
use Symfony\Component\Serializer\Serializer;
use Symfony\Component\HttpFoundation\Request;

class MobileApiController extends Controller
{

    public function LoginAction($username, $password)
    {

        $user_manager = $this->get('fos_user.user_manager');
        $factory = $this->get('security.encoder_factory');

        $user = $user_manager->findUserByUsername($username);
        $user->setBirthDate($user->getBirthDate()->format('Y-m-d'));
        $user->setRegistrationDate($user->getRegistrationDate()->format('Y-m-d'));
        $encoder = $factory->getEncoder($user);

        $users = $this->getDoctrine()->getRepository(User::class)->findBy(array('username'=>$username));
        $bool = ($encoder->isPasswordValid($user->getPassword(),$password,$user->getSalt())) ? "true" : "false";
        if($bool == "true" )
        {
            $serializer = new Serializer([new ObjectNormalizer()]);
            $formatted = $serializer->normalize($users);
            return new JsonResponse($formatted);
        }
        else
        {
            $serializer = new Serializer([new ObjectNormalizer()]);
            $formatted = $serializer->normalize(false);
            return new JsonResponse($formatted);
        }

    }

    public function fetchAction($id)
    {

        $em = $this->getDoctrine()->getManager();
        $user = $em->getRepository(User::class)->findBy(array('id' => $id));


        foreach ($user as $u)
        {
            $u->setBirthDate($u->getBirthDate()->format('Y-m-d'));
            $u->setRegistrationDate($u->getRegistrationDate()->format('Y-m-d'));
        }
        $serializer = new Serializer([new ObjectNormalizer()]);
        $formatted = $serializer->normalize($user);
        return new JsonResponse($formatted);


    }

    public function fetchPostsAction($id)
    {

        $em = $this->getDoctrine()->getManager();
        $user = $em->getRepository(Post::class)->findBy(array('topic' => $id));
        foreach ($user as $pst) {

            $pst->setUserI($pst->getUser()->getId());
            $pst->setAddedDate($pst->getAddedDate()->format('Y-m-d'));
        }
        $normalizer = new ObjectNormalizer();
        $normalizer->setCircularReferenceLimit(2);
        $normalizer->setCircularReferenceHandler(function ($object) {
            return $object->getId();
        });

        $serializer = new Serializer([$normalizer]);
        $formatted = $serializer->normalize($user);
        return new JsonResponse($formatted);


    }

    public function RegisterAction(Request $request)
    {

        $userManager = $this->get('fos_user.user_manager');

        $user = new User();
        $password = 'azerty';
        $user->setPlainPassword($password);

        $user->setUsername($request->get('username'));
        $user->setFirstName($request->get('firstName'));
        $user->setLastName($request->get('lastName'));
        $user->setEmail($request->get('email'));
        $user->setAddress($request->get('address'));
        $user->setCity($request->get('city'));
        $user->setZipCode($request->get('zipCode'));
        $user->setPhoneNumber($request->get('phoneNumber'));
        $user->setBirthDate(date_create($request->get('birthDate'),new  \DateTimeZone('America/New_York')));
        $user->setNewsLetterSubscribtion(false);

        $userManager->updateUser($user, true);
        $encryption = $user->getPassword();
        $serializer = new Serializer([new ObjectNormalizer()]);
        $formatted = $serializer->normalize($user);
        return new JsonResponse($formatted);
    }

    public function UpdateAction(Request $request)
    {
        $em = $this->getDoctrine()->getManager();
        $user = $em->getRepository(User::class)->find($request->get('id'));

        $subscribe = true;
        if($request->get('newsLetterSubscribtion') == 'false')
            $subscribe = false;

        $user->setUsername($request->get('username'));
        $user->setFirstName($request->get('firstName'));
        $user->setLastName($request->get('lastName'));
        $user->setEmail($request->get('email'));
        $user->setAddress($request->get('address'));
        $user->setCity($request->get('city'));
        $user->setZipCode($request->get('zipCode'));
        $user->setPhoneNumber($request->get('phoneNumber'));
        $user->setBirthDate(date_create($request->get('birthDate'),new  \DateTimeZone('America/New_York')));
        $user->setNewsLetterSubscribtion($subscribe);

        $em->persist($user);
        $em->flush();

        $serializer = new Serializer([new ObjectNormalizer()]);
        $formatted = $serializer->normalize($user);
        return new JsonResponse($formatted);
    }


    public function showTopicsAction()
    {
        $em = $this->getDoctrine()->getManager();
        $topics = $em->getRepository(Topic::class)->findAll();
        foreach ($topics as $topic)
        {
            $topic->setAddedDate($topic->getAddedDate()->format('Y-m-d'));
            $topic->setPostCount($this->getTopicPostsCount($topic));

        }
        $normalizer = new ObjectNormalizer();
        $normalizer->setCircularReferenceLimit(2);
        $normalizer->setCircularReferenceHandler(function ($object) {
            return $object->getId();
        });

        $serializer = new Serializer([$normalizer]);
        $formatted = $serializer->normalize($topics);
        return new JsonResponse($formatted);

    }

    public function showPinnedAction()
    {
        $em = $this->getDoctrine()->getManager();
        $topics = $em->getRepository(Topic::class)->findBy(array('pinned' => true));
        foreach ($topics as $topic)
        {
            $topic->setAddedDate($topic->getAddedDate()->format('Y-m-d'));
            $topic->setPostCount($this->getTopicPostsCount($topic));

        }
        $normalizer = new ObjectNormalizer();
        $normalizer->setCircularReferenceLimit(2);
        $normalizer->setCircularReferenceHandler(function ($object) {
            return $object->getId();
        });

        $serializer = new Serializer([$normalizer]);
        $formatted = $serializer->normalize($topics);
        return new JsonResponse($formatted);

    }


    private function getTopicPostsCount($topic)
    {
        //create an entity manager object
        $em = $this->getDoctrine()->getManager();
        $posts = $em->getRepository(Post::class)->findBy(array('topic'=>$topic));

        return count($posts);
    }

    public function deletePostAction($id)
    {

        $em = $this->getDoctrine()->getManager();
        $post = $em->getRepository(Post::class)->find($id);
        $normalizer = new ObjectNormalizer();
        $normalizer->setCircularReferenceLimit(2);
        $normalizer->setCircularReferenceHandler(function ($object) {
            return $object->getId();
        });
        $serializer = new Serializer([$normalizer]);
        $formatted = $serializer->normalize($post);

        $em->remove($post);
        $em->flush();

        return new JsonResponse($formatted);


    }

    public function ReplyAction($user_id, $post_id, $content)
    {
        $em = $this->getDoctrine()->getManager();
        $user = $em->getRepository(User::class)->find($user_id);
        $topic = $em->getRepository(Topic::class)->find($post_id);

        $post = new Post();
        $post->setUser($user);
        $post->setContent($content);
        $post->setTopic($topic);

        $em->persist($post);
        $em->flush();

        $normalizer = new ObjectNormalizer();
        $normalizer->setCircularReferenceLimit(2);
        $normalizer->setCircularReferenceHandler(function ($object) {
            return $object->getId();
        });
        $serializer = new Serializer([$normalizer]);
        $formatted = $serializer->normalize($post);



        return new JsonResponse($formatted);

    }



}
