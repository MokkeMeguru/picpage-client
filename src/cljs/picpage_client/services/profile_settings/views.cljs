(ns picpage-client.services.profile-settings.views
  (:require [reagent.core :as reagent]
            [re-frame.core :as re-frame]
            [picpage-client.subs :as subs]
            [clojure.spec.alpha :as s]
            [picpage-client.domain.users :as users-domain]))

(def max-file-size (* 5 1024 1024))

(defn load-image [preview-src preview-id]
  (fn [file-added-event]
    (.log js/console "load image")
    (let [file (first (array-seq (.. file-added-event -target -files)))
          file-reader (js/FileReader.)]
      (if (<= (.-size file) max-file-size)
        (do (set! (.-onload file-reader)
                  (fn [file-load-event]
                    (reset! preview-src (.. file-load-event -target -result))
                    (let [img (.getElementById js/document preview-id)]
                      (set! (.-onload img)
                            (fn [img-load]
                              (.log js/console "dimensions:" (.-width img) "x" (.-height img)))))))
            (.readAsDataURL file-reader file)
            true)
        false))))

(defn image-field [image image-error]
  [:div.container>div.columns
   [:div.column.is-6
    [:div.field
     [:label.label "アイコン画像 (5MB 以下)"]
     [:div.control
      [:input.input
       {:type "file" :accept "image/*"
        :on-change
        #(let [func (load-image image "preview")]
           (reset! image nil)
           (if (func %)
             (reset! image-error false)
             (reset! image-error true)))}]]
     (when @image-error [:div.help.is-danger  "ファイルを読み込めませんでした"])]]
   (when-not (empty? @image)
     [:div.column.is-6 [:figure.image
                        [:img#preview.is-rounded
                         {:src @image
                          :style {:width "10rem" :height "10rem" :object-fit "fill" :margin "auto"}}]]])])

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

(defn submit-field [image image-error
                    username username-error
                    submitted]
  [:div.field
   [:div.control.form
    [:button.button.is-primary
     {:on-click #(do
                   (.preventDefault %)
                   (reset! username-error (not (s/valid? ::users-domain/username @username)))
                   (if (and (not @username-error)
                            (not image-error))
                     (reset! submitted true)))}
     "submit"]]
   (if @submitted [:p "submitted"])])

(def profile-settings-content
  {:title "Profile Settings"
   :subtitle ""
   :contents (fn []
               (let [image (reagent/atom @(re-frame/subscribe [::subs/icon-image]))
                     image-error (reagent/atom false)
                     username (reagent/atom @(re-frame/subscribe [::subs/name]))
                     username-error (reagent/atom nil)
                     submitted (reagent/atom false)]
                 (fn []
                   [:div.container
                    [image-field image image-error]
                    [username-field username username-error]
                    [submit-field
                     image image-error
                     username username-error
                     submitted]])))})

(def profile-settings
  [:div.container
   [:div.titles
    [:p.title (:title profile-settings-content)]
    [:p.subtitle.is-6 (:subtitle profile-settings-content)]]
   [:div.container.my-3>div.content {:style {:max-width "512px" :margin "auto"}}
    [(:contents profile-settings-content)]]])
