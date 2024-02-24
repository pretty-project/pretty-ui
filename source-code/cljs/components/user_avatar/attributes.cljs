
(ns components.user-avatar.attributes
    (:require [fruits.css.api               :as css]
              [fruits.math.api              :as math]
              [pretty-attributes.api  :as pretty-attributes]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn avatar-color-attributes
  ; @ignore
  ;
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
  ; Do: W                    = 60px
  ; Di: W - 2 * stroke-width = 56px
  ; Ro: Do / 2               = 30px
  ; Ri: Di / 2               = 28px
  ; Rc: (Do + Di) / 2        = 29px
  ; CIRCUM: 2Rc * Pi         = 185.35
  (let [radius-center    (dec (/ size 2))
        circum           (math/radius->circum radius-center)
        percent          (/ 100 (count colors))
        percent-result   (math/percent-result circum        percent)
        percent-rem      (math/percent-result circum (- 100 percent))
        stroke-dasharray (str percent-result" "percent-rem)
        rotation-angle   (+ 30 (* dex (/ 360 (count colors))))]
       {:class :c-user-avatar--color
        :cx (/ size 2)
        :cy (/ size 2)
        :style {:transform (css/rotate-z rotation-angle)}
        :r                radius-center
        :stroke           color
        :stroke-dasharray stroke-dasharray
        :stroke-width     2}))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn avatar-inner-attributes
  ; @ignore
  ;
  ; @param (keyword) avatar-id
  ; @param (map) avatar-props
  ; {:size (px)}
  ;
  ; @return (map)
  ; {:class (keyword or keywords in vector)
  ;  :style (map)}
  [_ {:keys [size] :as avatar-props}]
  (-> {:class :c-user-avatar--inner
       :style {:height (css/px size)
               :width  (css/px size)}}
      (pretty-attributes/inner-space-attributes avatar-props)
      (pretty-attributes/style-attributes  avatar-props)))


;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn avatar-attributes
  ; @ignore
  ;
  ; @param (keyword) avatar-id
  ; @param (map) avatar-props
  ;
  ; @return (map)
  ; {}
  [_ avatar-props]
  (-> {:class :c-user-avatar}
      (pretty-attributes/class-attributes  avatar-props)
      (pretty-attributes/outer-space-attributes avatar-props)
      (pretty-attributes/state-attributes  avatar-props)))
