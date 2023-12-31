
(ns components.user-avatar.views
    (:require [components.user-avatar.attributes :as user-avatar.attributes]
              [components.user-avatar.prototypes :as user-avatar.prototypes]
              [fruits.css.api                    :as css]
              [fruits.random.api                 :as random]
              [fruits.string.api                 :as string]
              [fruits.svg.api                    :as svg]
              [fruits.vector.api                 :as vector]
              [pretty-elements.api               :as pretty-elements]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- user-avatar-colors
  ; @ignore
  ;
  ; @param (keyword) avatar-id
  ; @param (map) avatar-props
  ; {:colors (strings in vector)
  ;  :size (px)}
  [avatar-id {:keys [colors size] :as avatar-props}]
  (let [view-box (svg/view-box size size)]
       [:svg {:class :c-user-avatar--colors :view-box view-box :style {:width (css/px size) :height (css/px size)}}
             (letfn [(f0 [colors dex color]
                         (conj colors [:circle (user-avatar.attributes/avatar-color-attributes avatar-id avatar-props dex color)]))]
                    (reduce-kv f0 [:<>] colors))]))

(defn- user-avatar
  ; @ignore
  ;
  ; @param (keyword) avatar-id
  ; @param (map) avatar-props
  ; {:colors (strings in map)(opt)
  ;  :icon (keyword)(opt)
  ;  :icon-family (keyword)(opt)
  ;  :initials (string)(opt)}
  [avatar-id {:keys [colors icon icon-family initials] :as avatar-props}]
  [:div (user-avatar.attributes/avatar-attributes avatar-id avatar-props)
        [:div (user-avatar.attributes/avatar-body-attributes avatar-id avatar-props)
              (if (string/nonempty? initials) [:div {:class :c-user-avatar--initials :data-selectable false} initials]
                                              [:div {:class :c-user-avatar--icon :data-icon-family icon-family :data-icon-size :m} icon])
              (if (vector/nonempty? colors)   [user-avatar-colors avatar-id avatar-props])]])

(defn component
  ; @info
  ; XXX#0720
  ; Some other items based on the 'user-avatar' component and their documentations link here.
  ;
  ; @param (keyword)(opt) avatar-id
  ; @param (map) avatar-props
  ; {:class (keyword or keywords in vector)(opt)
  ;  :colors (strings in vector)(opt)
  ;  :disabled? (boolean)(opt)
  ;  :icon (keyword)(opt)
  ;   Default: :person
  ;  :icon-family (keyword)(opt)
  ;   Default: :material-symbols-outlined
  ;  :indent (map)(opt)
  ;   {:all, :bottom, :left, :right, :top, :horizontal, :vertical (keyword, px or string)(opt)}
  ;  :initials (string)(opt)
  ;   Displays initial letters instead of displaying an icon
  ;  :outdent (map)(opt)
  ;   {:all, :bottom, :left, :right, :top, :horizontal, :vertical (keyword, px or string)(opt)}
  ;  :size (px)(opt)
  ;   Default: 60
  ;  :style (map)(opt)
  ;  :width (px)(opt)}
  ;
  ; @usage
  ; [user-avatar {...}]
  ;
  ; @usage
  ; [user-avatar :my-user-avatar {...}]
  ([avatar-props]
   [component (random/generate-keyword) avatar-props])

  ([avatar-id avatar-props]
   ; @note (tutorials#parametering)
   (fn [_ avatar-props]
       (let [avatar-props (user-avatar.prototypes/avatar-props-prototype avatar-props)]
            [user-avatar avatar-id avatar-props]))))
