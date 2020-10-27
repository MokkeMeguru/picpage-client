(ns picpage-client.services.login.views
  (:require [reagent.core :as reagent]
            [picpage-client.domain.users :as users-domain]
            [picpage-client.services.login.events :as login-events]
            [picpage-client.services.login.subs :as login-subs]
            [clojure.spec.alpha :as s]
            ["react-google-recaptcha" :default ReCAPTCHA]
            [re-frame.core :as re-frame]
            [picpage-client.subs :as subs]))

(defn email-field [email email-error]
  [:div.field
   [:label "メールアドレス"]
   [:div.control.has-icons-left
    [:input.input {:type "email" :placeholder "メールアドレス"
                   :class (if @email-error "is-danger" "is-success")
                   :on-change (fn [e]
                                (reset! email (.. e -target -value)))}]
    [:span.icon.is-samll.is-left [:i.fas.fa-envelope]]]])

(defn password-field [password password-error]
  [:div.field
   [:label "パスワード"]

   [:div.control.has-icons-left.has-icons-right
    [:input.input {:type "password" :placeholder "パスワード"
                   :class (if @password-error "is-danger" "is-success")
                   :on-change (fn [e]
                                (reset! password (.. e -target -value)))}]
    [:span.icon.is-samll.is-left [:i.fas.fa-lock]]]])

(defn submit-field [email email-error
                    password password-error
                    submitted]
  [:div.field
   [:div.control.form
    [:button.button.is-primary
     {:on-click #(do
                   (.preventDefault %)
                   (reset! email-error (not (s/valid? ::users-domain/email @email)))
                   (reset! password-error (not (s/valid? ::users-domain/password @password)))
                   (if (and (not @email-error)
                            (not @password-error))
                     (do
                       (re-frame/dispatch [::login-events/login @email @password])
                       (reset! submitted true))))}
     "submit"]]
   (when @(re-frame/subscribe [::subs/show-twirly]) [:p "submitting..."])
   (if-let [status @(re-frame/subscribe [::login-subs/login-status])]
     (if (= status 1)
       [:p "login success!"]
       [:p "login failed ..."]))])

(def login-content
  {:title "Login"
   :subtitle "メールアドレス と パスワードを用いてログインします。"
   :contents (fn []
               (let [email (reagent/atom "")
                     password (reagent/atom "")

                     email-error (reagent/atom nil)
                     password-error (reagent/atom nil)
                     submitted (reagent/atom false)
                     captured (reagent/atom nil)]
                 (fn []
                   [:div.container
                    [email-field email email-error]
                    [password-field  password password-error]
                    [submit-field
                     email email-error
                     password password-error
                     submitted]])))})

(def login
  [:div.container
   [:div.titles
    [:p.title (:title login-content)]
    [:p.subtitle.is-6 (:subtitle login-content)]]
   [:div.container.my-3>div.content {:style {:max-width "512px" :margin "auto"}}
    [(:contents login-content)]]])
