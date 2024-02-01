
(ns pretty-inputs.header.views
    (:require [fruits.random.api :as random]
              [pretty-inputs.header.adornments :as header.adornments]
              [pretty-inputs.header.attributes :as header.attributes]
              [pretty-inputs.header.prototypes :as header.prototypes]
              [pretty-elements.api :as pretty-elements]
              [pretty-elements.engine.api :as pretty-elements.engine]
              [pretty-inputs.engine.api :as pretty-inputs.engine]
              [pretty-presets.engine.api :as pretty-presets.engine]
              [reagent.api :as reagent]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn header-error-text
  ; @ignore
  ;
  ; @param (keyword) header-id
  ; @param (map) header-props
  ; {:error-text (metamorphic-content)(opt)}
  [header-id {:keys [error-text] :as header-props}]
  (if error-text [:div (header.attributes/header-error-text-attributes header-id header-props)
                       (-> error-text)]))

(defn- header-helper-text
  ; @ignore
  ;
  ; @param (keyword) header-id
  ; @param (map) header-props
  ; {:helper-text (metamorphic-content)(opt)}
  [header-id {:keys [helper-text] :as header-props}]
  (if helper-text [:div (header.attributes/header-helper-text-attributes header-id header-props)
                        (-> helper-text)]))

(defn header-info-text
  ; @ignore
  ;
  ; @param (keyword) header-id
  ; @param (map) header-props
  ; {:info-text (metamorphic-content)(opt)}
  [header-id {:keys [info-text] :as header-props}]
  (if info-text (if-let [info-text-visible? (pretty-inputs.engine/input-info-text-visible? header-id header-props)]
                        [:div (header.attributes/header-info-text-attributes header-id header-props)
                              (-> info-text)])))

(defn header-info-button
  ; @ignore
  ;
  ; @param (keyword) header-id
  ; @param (map) header-props
  ; {:info-text (metamorphic-content)(opt)}
  [header-id {:keys [info-text] :as header-props}]
  (if info-text (let [toggle-info-text-adornment (header.adornments/toggle-info-text-adornment header-id header-props)]
                     [pretty-elements/adornment toggle-info-text-adornment])))

(defn- header-label
  ; @ignore
  ;
  ; @param (keyword) header-id
  ; @param (map) header-props
  ; {:label (metamorphic-content)(opt)}
  [header-id {:keys [label] :as header-props}]
  ; https://css-tricks.com/html-inputs-and-labels-a-love-story/
  ; ... it is always the best idea to use an explicit label instead of an implicit label.
  (if label [:div (header.attributes/header-label-attributes header-id header-props) label
                  [header-info-button                        header-id header-props]]))

(defn- header
  ; @ignore
  ;
  ; @param (keyword) header-id
  ; @param (map) header-props
  [header-id header-props]
  [:div (header.attributes/header-attributes header-id header-props)
        [:div (header.attributes/header-body-attributes header-id header-props)
              [header-label       header-id header-props]
              [header-info-text   header-id header-props]
              [header-helper-text header-id header-props]
              [header-error-text  header-id header-props]]])

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- view-lifecycles
  ; @ignore
  ;
  ; @param (keyword) header-id
  ; @param (map) header-props
  [header-id header-props]
  ; @note (tutorials#parameterizing)
  (reagent/lifecycles {:component-did-mount    (fn [_ _] (pretty-elements.engine/element-did-mount    header-id header-props))
                       :component-will-unmount (fn [_ _] (pretty-elements.engine/element-will-unmount header-id header-props))
                       :reagent-render         (fn [_ header-props] [header header-id header-props])}))

(defn view
  ; @note
  ; To associate the 'header' element with an input, use the same ID as for the input.
  ;
  ; @param (keyword)(opt) header-id
  ; @param (map) header-props
  ; {:class (keyword or keywords in vector)(opt)
  ;  :disabled? (boolean)(opt)
  ;  :error-text (metamorphic-content)(opt)
  ;  :helper-text (metamorphic-content)(opt)
  ;  :indent (map)(opt)
  ;   {:all, :bottom, :left, :right, :top, :horizontal, :vertical (keyword, px or string)(opt)}
  ;  :info-text (metamorphic-content)(opt)
  ;  :label (metamorphic-content)(opt)
  ;  :line-height (keyword, px or string)(opt)
  ;  :marker-color (keyword or string)(opt)
  ;  :marker-position (keyword)(opt)
  ;  :on-mount-f (function)(opt)
  ;  :on-unmount-f (function)(opt)
  ;  :outdent (map)(opt)
  ;   {:all, :bottom, :left, :right, :top, :horizontal, :vertical (keyword, px or string)(opt)}
  ;  :preset (keyword)(opt)
  ;  :style (map)(opt)
  ;  :theme (keyword)(opt)}
  ;
  ; @usage
  ; [header {...}]
  ;
  ; @usage
  ; [header :my-header {...}]
  ([header-props]
   [view (random/generate-keyword) header-props])

  ([header-id header-props]
   ; @note (tutorials#parameterizing)
   (fn [_ header-props]
       (let [header-props (pretty-presets.engine/apply-preset           header-id header-props)
             header-props (header.prototypes/header-props-prototype     header-id header-props)
             header-props (pretty-inputs.engine/import-input-error-text header-id header-props)]
            [view-lifecycles header-id header-props]))))
