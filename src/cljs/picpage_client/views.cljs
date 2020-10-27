(ns picpage-client.views
  (:require
   [re-frame.core :as re-frame]
   [picpage-client.subs :as subs]
   [picpage-client.services.header.views :as header-views]
   [picpage-client.services.footer.views :as footer-views]
   [picpage-client.events :as events]))

(defn title-panel [name]
  [:header.bd-header>div.bd-header-titles
   [:h1.title "Welcome to Picpage"]
   [:p.subtitle.is-4 "for " (if name name "Anonymous User")]])

(defn error-panel [error-message]
  [:div.modal.is-active
   [:div.modal-background]
   [:div.modal-card
    [:header.modal-card-head.bd-warn [:p.modal-card-title "エラー"]
     [:button.delete {:aria-label "close" :on-click #(re-frame/dispatch [::events/delete-error])}]]
    [:section.modal-card-body.bd-warn [:div.px-5>p error-message]]
    [:footer.modal-card-foot.bd-warn {:style {:display "block"}}
     [:div.has-text-left footer-views/footer-content]]]])

(defn main-panel []
  (let [name (re-frame/subscribe [::subs/name])
        login? (re-frame/subscribe [::subs/login?])
        current-route (re-frame/subscribe [::subs/current-route])
        error-message (re-frame/subscribe [::subs/error])]
    [:div
     [header-views/header @login?]
     [:main.bd-main
      [:div.bd-main-container.container
       [:div.duo
        [:div.bd-lead
         (if @error-message [error-panel @error-message])
         [title-panel @name]
         [:div (when @current-route (-> @current-route :data :view))]]]]]
     footer-views/footer]))

;; (re-frame/subscribe [:current-route])
