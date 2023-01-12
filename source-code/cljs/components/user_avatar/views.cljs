
(ns components.user-avatar.views
    (:require [components.user-avatar.helpers    :as user-avatar.helpers]
              [components.user-avatar.prototypes :as user-avatar.prototypes]
              [css.api                           :as css]
              [elements.api                      :as elements]
              [random.api                        :as random]
              [string.api                        :as string]
              [svg.api                           :as svg]
              [vector.api                        :as vector]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- user-avatar-color
  ; @param (keyword) avatar-id
  ; @param (map) avatar-props
  ; @param (integer) dex
  ; @param (string) color
  [avatar-id avatar-props dex color]
  [:circle.c-user-avatar--color (user-avatar.helpers/avatar-color-attributes avatar-id avatar-props dex color)])

(defn- user-avatar-colors
  ; @param (keyword) avatar-id
  ; @param (map) avatar-props
  ; {:colors (strings in vector)
  ;  :size (px)}
  [avatar-id {:keys [colors size] :as avatar-props}]
  (let [view-box (svg/view-box size size)]
       [:svg.c-user-avatar--colors {:view-box view-box :style {:width (css/px size) :height (css/px size)}}
                                   (letfn [(f [colors dex color] (conj colors [user-avatar-color avatar-id avatar-props dex color]))]
                                          (reduce-kv f [:<>] colors))]))

(defn- user-avatar-icon
  ; @param (keyword) avatar-id
  ; @param (map) avatar-props
  [_ _]
  [:div.c-user-avatar--icon {:data-icon-family :material-icons-outlined
                             :data-icon-size   :m}
                            :person])

(defn- user-avatar-initials
  ; @param (keyword) avatar-id
  ; @param (map) avatar-props
  ; {:initials (string)}
  [_ {:keys [initials]}]
  [:div.c-user-avatar--initials {:data-selectable false} initials])

(defn- user-avatar-body
  ; @param (keyword) avatar-id
  ; @param (map) avatar-props
  ; {:colors (strings in map)(opt)
  ;  :initials (string)(opt)
  ;  :size (px)}
  [avatar-id {:keys [colors initials size] :as avatar-props}]
  [:div.c-user-avatar--body (user-avatar.helpers/avatar-body-attributes avatar-id avatar-props)
                            (if (string/nonblank? initials) [user-avatar-initials avatar-id avatar-props]
                                                            [user-avatar-icon     avatar-id avatar-props])
                            (if (vector/nonempty? colors)   [user-avatar-colors   avatar-id avatar-props])])

(defn- user-avatar
  ; @param (keyword) avatar-id
  ; @param (map) avatar-props
  [avatar-id avatar-props]
  [:div.c-user-avatar (user-avatar.helpers/avatar-attributes avatar-id avatar-props)
                      [user-avatar-body avatar-id avatar-props]])

(defn component
  ; @param (keyword)(opt) avatar-id
  ; @param (map) avatar-props
  ; {:class (keyword or keywords in vector)(opt)
  ;  :colors (strings in vector)(opt)
  ;  :disabled? (boolean)(opt)
  ;   Default: false
  ;  :first-name (string)(opt)
  ;  :indent (map)(opt)
  ;  :outdent (map)(opt)
  ;  :last-name (string)(opt)
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
   (let [avatar-props (user-avatar.prototypes/avatar-props-prototype avatar-props)]
        [user-avatar avatar-id avatar-props])))
