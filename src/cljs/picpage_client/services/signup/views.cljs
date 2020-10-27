(ns picpage-client.services.signup.views
  (:require [reagent.core :as reagent]
            [picpage-client.domain.users :as users-domain]
            [clojure.spec.alpha :as s]
            ["react-google-recaptcha" :default ReCAPTCHA]
            [re-frame.core :as re-frame]
            [picpage-client.services.signup.events :as signup-events]
            [picpage-client.services.signup.subs :as signup-subs]
            [picpage-client.subs :as subs]))

(defn username-field [username username-error]
  [:div.field
   [:label.label "ユーザ名"]
   [:div.control.has-icons-left
    [:input.input {:type "text" :placeholder "ユーザ名 (4文字 ~ 18文字)"
                   :class (if @username-error "is-danger" "is-success")
                   :on-change (fn [e]
                                (reset! username (.. e -target -value)))}]
    [:span.icon.is-samll.is-left [:i.fas.fa-user]]]
   (if @username-error
     [:div.help.is-danger
      "ユーザ名が不正です。(4文字 ~ 18文字)"])])

(defn userid-field [userid userid-error]
  [:div.field
   [:label.label "ユーザID"]
   [:div.control.has-icons-left
    [:input.input {:type "text" :placeholder "ユーザID (英数字4文字 ~ 18文字)"
                   :class (if @userid-error "is-danger" "is-success")
                   :on-change (fn [e]
                                (reset! userid (.. e -target -value)))}]
    [:span.icon.is-samll.is-left [:i.fas.fa-id-card]]]
   (if @userid-error
     [:div.help.is-danger
      "ユーザIDが不正です。(英字4文字 ~ 18文字)"])])

(defn email-field [email email-error]
  [:div.field
   [:label.label "メールアドレス"]
   [:div.control.has-icons-left
    [:input.input {:type "email" :placeholder "メールアドレス"
                   :class (if @email-error "is-danger" "is-success")
                   :on-change (fn [e]
                                (reset! email (.. e -target -value)))}]
    [:span.icon.is-samll.is-left [:i.fas.fa-envelope]]]
   (if @email-error
     [:div.help.is-danger
      "メールアドレスが不正です。"])])

(defn password-field [password password-error]
  [:div.field
   [:label.label "パスワード"]
   [:div.control.has-icons-left.has-icons-right
    [:input.input {:type "password" :placeholder "パスワード (6文字 ~ 18文字 英小文字+英大文字+数字)"
                   :class (if @password-error "is-danger" "is-success")
                   :on-change (fn [e]
                                (reset! password (.. e -target -value)))}]
    [:span.icon.is-samll.is-left [:i.fas.fa-lock]]]
   (if @password-error
     [:div.help.is-danger
      "パスワードが不正です。(6文字 ~ 18文字)"])])

(defn submit-field [username username-error
                    userid userid-error
                    email email-error
                    password password-error
                    captured
                    submitted]
  [:div.field
   [:div.control.form
    [:> ReCAPTCHA {:sitekey "6LeiG9sZAAAAAA9hS-VeHc6XyXvkzmJ_MO3Wwr6f"
                   :on-change #(reset! captured %)}]
    [:br]
    [:button.button.is-primary
     {:on-click #(do
                   (.preventDefault %)
                   (reset! username-error (not (s/valid? ::users-domain/username @username)))
                   (reset! userid-error (not (s/valid? ::users-domain/userid @userid)))
                   (reset! email-error (not (s/valid? ::users-domain/email @email)))
                   (reset! password-error (not (s/valid? ::users-domain/password @password)))
                   (if (and (not @username-error)
                            (not @email-error)
                            (not @password-error)
                            (not (empty? @captured)))
                     (do
                       (re-frame/dispatch [::signup-events/signup {:name @username
                                                                   :user_id @userid
                                                                   :password @password
                                                                   :email @email}])
                       (reset! submitted true))))}
     "submit"]]
   (when @(re-frame/subscribe [::subs/show-twirly]) [:p "submitting..."])
   (if-let [status @(re-frame/subscribe [::signup-subs/signup-status])]
     (if (= status 1)
       [:p "submit succeed!"]
       [:p "submit failed..."]))])

(def signup-content
  {:title "Signup"
   :subtitle "ユーザ名, ユーザID, メールアドレス と パスワードを用いてユーザアカウントを作成します。"
   :contents (fn []
               (let [username (reagent/atom "")
                     userid (reagent/atom "")
                     email (reagent/atom "")
                     password (reagent/atom "")
                     username-error (reagent/atom nil)
                     userid-error (reagent/atom nil)
                     email-error (reagent/atom nil)
                     password-error (reagent/atom nil)
                     submitted (reagent/atom false)
                     captured (reagent/atom nil)]
                 (fn []
                   [:div.container
                    [username-field username username-error]
                    [userid-field userid userid-error]
                    [email-field email email-error]
                    [password-field  password password-error]
                    [submit-field
                     username username-error
                     userid userid-error
                     email email-error
                     password password-error
                     captured
                     submitted]])))})

(def signup
  [:div.container
   [:div.titles
    [:p.title (:title signup-content)]
    [:p.subtitle.is-6 (:subtitle signup-content)]]
   [:div.container.my-3>div.content {:style {:max-width "512px" :margin "auto"}}
    [(:contents signup-content)]]])
