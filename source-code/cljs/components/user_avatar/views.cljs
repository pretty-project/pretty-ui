
(ns components.user-avatar.views
    (:require [components.user-avatar.helpers    :as user-avatar.helpers]
              [components.user-avatar.prototypes :as user-avatar.prototypes]
              [css.api                           :as css]
              [elements.api                      :as elements]
              [math.api                          :as math]
              [random.api                        :as random]
              [string.api                        :as string]
              [svg.api                           :as svg]
              [vector.api                        :as vector]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- user-avatar-color
  ; @param (keyword) avatar-id
  ; @param (map) avatar-props
  ; {:colors (strings in vector)
  ;  :size (px)}
  ; @param (integer) dex
  ; @param (string) color
  [_ {:keys [colors size]} dex color]
  ; W:  60px
  ; H:  120px
  ; Do: W                 = 60px
  ; Di: W - 2stroke-width = 56px
  ; Ro: Do / 2            = 30px
  ; Ri: Di / 2            = 28px
  ; Rc: (Do + Di) / 2     = 29px
  ; CIRCUM: 2Rc * Pi      = 185.35
  (let [radius-center    (dec (/ size 2))
        circum           (math/circum radius-center)
        percent          (/ 100 (count colors))
        percent-result   (math/percent-result circum        percent)
        percent-rem      (math/percent-result circum (- 100 percent))
        stroke-dasharray (str percent-result" "percent-rem)
        rotation-angle   (+ 30 (* dex (/ 360 (count colors))))]
       [:circle.c-user-avatar--color {:style {:transform (css/rotate-z rotation-angle)}
                                      :r                radius-center
                                      :stroke           color
                                      :stroke-dasharray stroke-dasharray
                                      :stroke-width     2
                                      :cx (/ size 2)
                                      :cy (/ size 2)}]))

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
  [:div.c-user-avatar--icon [elements/icon {:icon :person}]])

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
