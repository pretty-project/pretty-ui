
(ns elements.slider.attributes
    (:require [css.api                 :as css]
              [pretty-css.api          :as pretty-css]
              [elements.slider.helpers :as slider.helpers]
              [elements.slider.state   :as slider.state]
              [re-frame.api            :as r]))

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
       {:class             :e-slider--thumb
        :data-click-effect :opacity
        :on-mouse-down     #(slider.helpers/start-sliding! % slider-id :primary)
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
       {:class             :e-slider--thumb
        :data-click-effect :opacity
        :on-mouse-down     #(slider.helpers/start-sliding! % slider-id :secondary)
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
       {:class             :e-slider--line
        :data-click-effect :opacity
        :style {:width "200px"}}))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn slider-body-attributes
  ; @ignore
  ;
  ; @param (keyword) slider-id
  ; @param (map) slider-props
  ; {:style (map)(opt)}
  ;
  ; @return (map)
  ; {:class (keyword or keywords in vector)
  ;  :style (map)}
  [_ {:keys [style] :as slider-props}]
  (-> {:class :e-slider--body
       :style style}
      (pretty-css/indent-attributes slider-props)))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn slider-attributes
  ; @ignore
  ;
  ; @param (keyword) slider-id
  ; @param (map) slider-props
  ;
  ; @return (map)
  ; {}
  [_ slider-props]
  (-> {:class :e-slider}
      (pretty-css/default-attributes slider-props)
      (pretty-css/outdent-attributes slider-props)))