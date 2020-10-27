(ns picpage-client.services.pictures.views
  (:require [re-frame.core :as re-frame]
            [picpage-client.subs :as subs]
            [picpage-client.services.pictures.subs :as pictures-subs]
            [picpage-client.services.pictures.events :as pictures-events]))

(defn picture-card [picture]
  (let [api-url @(re-frame/subscribe [::subs/api-url])
        userid @(re-frame/subscribe [::subs/current-page])
        link-base   (str api-url
                         "api/authed/users/"
                         userid
                         "/pictures/"
                         (:picture_id picture))
        thumb-link  (str link-base
                         "/thumb")
        picture-link (str "#/users/" userid "/pictures/" (:picture_id picture))]
    [:div.card
     [:div.card-image
      [:figure.image
       [:img {:src thumb-link}]]
      [:div.card-content
       [:div.media-content
        [:a {:href picture-link} [:p.title.is-4 (:title picture)]]]
       [:div.content
        [:p "最終更新日:" [:time (let [date (if-not (= "" (:updated_at picture))
                                         (:updated_at picture) (:created_at picture))]
                              (first (clojure.string/split date " ")))]]]]]]))

(def pictures-content
  {:title "PicPage"
   :subtitle [(fn [] (str "PicPage @" @(re-frame/subscribe [::subs/current-page])))]
   :contents (fn []
               (let [_ (re-frame/dispatch [::pictures-events/login @(re-frame/subscribe [::subs/current-page])])
                     pictures (re-frame/subscribe [::pictures-subs/pictures])]
                 (fn []
                   (vec
                    (concat
                     [:div.container>div.columns]
                     (map
                      ;; (fn [_]
                      ;;   [:img {:src "http://localhost:3000/api/authed/users/meguru/pictures/1/thumb"}])
                      picture-card
                      @pictures))))))})

(def pictures
  [:div.container
   [:div.titles
    [:p.title (:title pictures-content)]
    [:p.subtitle.is-6 (:subtitle pictures-content)]]
   [:div.container.my-3>div.content {:style {:max-width "512px" :margin "auto"}}
    [(:contents pictures-content)]]])

;; @(re-frame/subscribe [::pictures-subs/pictures])
;; (vec
;;  (concat
;;   [:div.container
;;    [:p "contents"]]
;;   (map (fn [_]
;;          [:img {:src "http://localhost:3000/api/authed/users/meguru/pictures/1/thumb"}]) [1 2 3])))
