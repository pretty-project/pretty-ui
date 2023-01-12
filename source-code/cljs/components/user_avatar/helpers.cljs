
(ns components.user-avatar.helpers
    (:require [components.component.helpers :as component.helpers]
              [css.api                      :as css]
              [math.api                     :as math]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn avatar-color-attributes
  ; @param (keyword) avatar-id
  ; @param (map) avatar-props
  ; {:colors (strings in vector)
  ;  :size (px)}
  ; @param (integer) dex
  ; @param (string) color
  ;
  ; @return (map)
  ; {}
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
       {:cx (/ size 2)
        :cy (/ size 2)
        :style {:transform (css/rotate-z rotation-angle)}
        :r                radius-center
        :stroke           color
        :stroke-dasharray stroke-dasharray
        :stroke-width     2}))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn avatar-body-attributes
  ; @param (keyword) avatar-id
  ; @param (map) avatar-props
  ; {:size (px)
  ;  :style (map)(opt)}
  ;
  ; @return (map)
  ; {:style (map)}
  [avatar-id {:keys [size style] :as avatar-props}]
  (merge (component.helpers/component-indent-attributes avatar-id avatar-props)
         {:style (merge style {:height (css/px size)
                               :width  (css/px size)})}))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn avatar-attributes
  ; @param (keyword) avatar-id
  ; @param (map) avatar-props
  ;
  ; @return (map)
  [avatar-id avatar-props]
  (merge (component.helpers/component-default-attributes avatar-id avatar-props)
         (component.helpers/component-outdent-attributes avatar-id avatar-props)))
