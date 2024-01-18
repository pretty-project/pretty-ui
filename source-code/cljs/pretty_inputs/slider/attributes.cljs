
(ns pretty-inputs.slider.attributes
    (:require [fruits.css.api                    :as css]
              [pretty-build-kit.api              :as pretty-build-kit]
              [pretty-inputs.slider.side-effects :as slider.side-effects]
              [pretty-inputs.slider.state        :as slider.state]
              [re-frame.api                      :as r]))

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
  (-> {:class :pi-slider--body
       :style style}
      (pretty-build-kit/element-size-attributes slider-props)
      (pretty-build-kit/indent-attributes       slider-props)))

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
  (-> {:class :pi-slider}
      (pretty-build-kit/class-attributes        slider-props)
      (pretty-build-kit/outdent-attributes      slider-props)
      (pretty-build-kit/state-attributes        slider-props)
      (pretty-build-kit/wrapper-size-attributes slider-props)))
