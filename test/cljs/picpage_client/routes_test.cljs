(ns picpage-client.routes-test
  (:require [picpage-client.routes :as sut]
            [picpage-client.routes.spec :as spec]
            [clojure.spec.alpha :as s]
            [cljs.test :as t :include-macros true]

            [reitit.core :as r]))

(t/deftest routing-home
  (t/is (s/valid? ::spec/home (r/match-by-path sut/router "/"))))

(t/deftest routing-login
  (t/is (s/valid? ::spec/login (r/match-by-path sut/router "/login"))))

(t/deftest routing-logout
  (t/is (s/valid? ::spec/logout (r/match-by-path sut/router "/logout"))))

(t/deftest routing-signup
  (t/is (s/valid? ::spec/signup (r/match-by-path sut/router "/signup"))))

(t/deftest routing-upload
  (t/is (s/valid? ::spec/upload (r/match-by-path sut/router "/upload"))))

(t/deftest routing-profile-settings
  (t/is (s/valid? ::spec/profile-settings (r/match-by-path sut/router "/settings/profile"))))

(t/deftest routing-user-pictures
  (let [route-info (r/match-by-path sut/router "/users/sample-user/pictures")]
    (t/testing "router test"
      (t/is (s/valid? ::spec/user-pictures route-info)))
    (t/testing "params test"
      (t/is (= (-> route-info :path-params :name) "sample-user")))))

(t/deftest routing-user-picture
  (let [route-info (r/match-by-path sut/router "/users/sample-user/pictures/sample-id")]
    (t/testing "router test"
      (t/is (s/valid? ::spec/picture route-info)))
    (t/testing "params test"
      (t/is (= (-> route-info :path-params :name) "sample-user"))
      (t/is (= (-> route-info :path-params :id) "sample-id")))))
