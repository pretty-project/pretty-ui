
(ns pretty-inputs.slider.attributes
    (:require [fruits.css.api                    :as css]
              [pretty-attributes.api             :as pretty-attributes]
              [pretty-inputs.slider.side-effects :as slider.side-effects]
              [pretty-inputs.slider.state        :as slider.state]
              [re-frame.extra.api                :as r]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn slider-primary-thumb-attributes
  ; @ignore
  ;
  ; @param (keyword) slider-id
  ; @param (map) slider-props
  ; {}
  ;
  ; @return (map)
  ; {}
  [slider-id {:keys []}]
  (let [translate-x (get-in @slider.state/THUMBS [slider-id :primary :current-translate-x] 0)]
       {:class             :pi-slider--thumb
        :data-click-effect :opacity
        :on-mouse-down     #(slider.side-effects/start-sliding! % slider-id :primary)
        :style             {:left      (->           0 css/px)
                            :transform (-> translate-x css/px css/translate-x)}}))

(defn slider-secondary-thumb-attributes
  ; @ignore
  ;
  ; @param (keyword) slider-id
  ; @param (map) slider-props
  ; {}
  ;
  ; @return (map)
  ; {}
  [slider-id slider-props]
  (let [translate-x (get-in @slider.state/THUMBS [slider-id :secondary :current-translate-x] 0)]
       {:class             :pi-slider--thumb
        :data-click-effect :opacity
        :on-mouse-down     #(slider.side-effects/start-sliding! % slider-id :secondary)
        :style             {:right     (->           0 css/px)
                            :transform (-> translate-x css/px css/translate-x)}}))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn slider-line-attributes
  ; @ignore
  ;
  ; @param (keyword) slider-id
  ; @param (map) slider-props
  ; {}
  ;
  ; @return (map)
  ; {}
  [slider-id slider-props]
  (let []
       {:class             :pi-slider--line
        :data-click-effect :opacity
        :style {:width "200px"}}))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn inner-attributes
  ; @ignore
  ;
  ; @param (keyword) slider-id
  ; @param (map) slider-props
  ;
  ; @return (map)
  ; {:class (keyword or keywords in vector)
  ;  ...}
  [_ slider-props]
  (-> {:class :pi-slider--inner}
      (pretty-attributes/inner-space-attributes slider-props)
      (pretty-attributes/inner-size-attributes slider-props)
      (pretty-attributes/style-attributes  slider-props)))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn outer-attributes
  ; @ignore
  ;
  ; @param (keyword) slider-id
  ; @param (map) slider-props
  ;
  ; @return (map)
  ; {}
  [_ slider-props]
  (-> {:class :pi-slider--outer}
      (pretty-attributes/class-attributes       slider-props)
      (pretty-attributes/state-attributes       slider-props)
      (pretty-attributes/outer-size-attributes slider-props)
      (pretty-attributes/outer-space-attributes  slider-props)
      (pretty-attributes/theme-attributes        slider-props)))
