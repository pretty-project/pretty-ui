
(ns pretty-elements.adornment-group.attributes
    (:require [pretty-build-kit.api :as pretty-build-kit]
              [pretty-inputs.text-field.side-effects :as text-field.side-effects]
              [pretty-inputs.text-field.env :as text-field.env]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn adornment-label-attributes
  ; @ignore
  ;
  ; @param (keyword) adornment-id
  ; @param (map) adornment-props
  ;
  ; @return (map)
  ; {}
  [_ _]
  (-> {:class               :pe-adornment-group--adornment-label
       :data-font-size      :xxs
       :data-letter-spacing :auto
       :data-line-height    :text-block}))

(defn adornment-icon-attributes
  ; @ignore
  ;
  ; @param (keyword) adornment-id
  ; @param (map) adornment-props
  ;
  ; @return (map)
  ; {}
  [adornment-id adornment-props]
  (-> {:class :pe-adornment-group--adornment-icon}
      (pretty-build-kit/icon-attributes adornment-props)))

(defn adornment-attributes
  ; @ignore
  ;
  ; @param (keyword) adornment-id
  ; @param (map) adornment-props
  ;
  ; @return (map)
  ; {}
  [adornment-id adornment-props]
  (-> {:class :pe-adornment-group--adornment}

      (pretty-build-kit/color-attributes        adornment-props)
      (pretty-build-kit/cursor-attributes       adornment-props)
      (pretty-build-kit/effect-attributes       adornment-props)
      (pretty-build-kit/font-attributes         adornment-props)
      (pretty-build-kit/mouse-event-attributes  adornment-props)
      (pretty-build-kit/state-attributes        adornment-props)
      (pretty-build-kit/tab-attributes          adornment-props)
      (pretty-build-kit/tooltip-attributes      adornment-props)
      (pretty-build-kit/unselectable-attributes adornment-props)))

(defn countdown-adornment-attributes
  ; @ignore
  ;
  ; @param (keyword) adornment-id
  ; @param (map) adornment-props
  ;
  ; @return (map)
  [adornment-id adornment-props]
  (adornment-attributes adornment-id (-> adornment-props (dissoc :click-effect :hover-effect :icon-family :icon-size :on-click-f)
                                                         (assoc  :text-color :highlight))))
