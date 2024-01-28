
(ns pretty-inputs.slider.attributes
    (:require [fruits.css.api                    :as css]
              [pretty-css.appearance.api         :as pretty-css.appearance]
              [pretty-css.basic.api              :as pretty-css.basic]
              [pretty-css.layout.api             :as pretty-css.layout]
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
  ;
  ; @return (map)
  ; {:class (keyword or keywords in vector)}
  [_ slider-props]
  (-> {:class :pi-slider--body}
      (pretty-css.layout/element-size-attributes slider-props)
      (pretty-css.layout/indent-attributes       slider-props)
      (pretty-css.basic/style-attributes        slider-props)))

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
      (pretty-css.basic/class-attributes        slider-props)
      (pretty-css.layout/outdent-attributes      slider-props)
      (pretty-css.basic/state-attributes        slider-props)
      (pretty-css.appearance/theme-attributes        slider-props)
      (pretty-css.layout/wrapper-size-attributes slider-props)))
