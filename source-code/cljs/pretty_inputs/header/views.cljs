
(ns pretty-inputs.header.views
    (:require [fruits.random.api               :as random]
              [pretty-elements.api             :as pretty-elements]
              [pretty-elements.engine.api      :as pretty-elements.engine]
              [pretty-inputs.engine.api        :as pretty-inputs.engine]
              [pretty-inputs.header.adornments :as header.adornments]
              [pretty-inputs.header.attributes :as header.attributes]
              [pretty-inputs.header.prototypes :as header.prototypes]
              [pretty-presets.engine.api       :as pretty-presets.engine]
              [reagent.core :as reagent]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- header
  ; @ignore
  ;
  ; @param (keyword) header-id
  ; @param (map) header-props
  ; {:error-text (metamorphic-content)(opt)
  ;  :helper-text (metamorphic-content)(opt)
  ;  :info-text (metamorphic-content)(opt)
  ;  :label (metamorphic-content)(opt)}
  [header-id {:keys [error-text helper-text info-text label] :as header-props}]
  ; https://css-tricks.com/html-inputs-and-labels-a-love-story/
  ; ... it is always the best idea to use an explicit label instead of an implicit label.
  [:div (header.attributes/header-attributes header-id header-props)
        [:div (header.attributes/header-inner-attributes header-id header-props)
              (let [info-text-visible?         (pretty-inputs.engine/input-info-text-visible? header-id header-props)
                    toggle-info-text-adornment (header.adornments/toggle-info-text-adornment  header-id header-props)]
                   [:<> (if label              [:div (header.attributes/header-label-attributes header-id header-props) label
                                                     (if info-text [pretty-elements/adornment toggle-info-text-adornment])])
                        (if info-text-visible? [:div (header.attributes/header-info-text-attributes   header-id header-props) info-text])
                        (if helper-text        [:div (header.attributes/header-helper-text-attributes header-id header-props) helper-text])
                        (if error-text         [:div (header.attributes/header-error-text-attributes  header-id header-props) error-text])])]])

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- view-lifecycles
  ; @ignore
  ;
  ; @param (keyword) header-id
  ; @param (map) header-props
  [header-id header-props]
  ; @note (tutorials#parameterizing)
  (reagent/create-class {:component-did-mount    (fn [_ _] (pretty-elements.engine/element-did-mount    header-id header-props))
                         :component-will-unmount (fn [_ _] (pretty-elements.engine/element-will-unmount header-id header-props))
                         :reagent-render         (fn [_ header-props] [header header-id header-props])}))

(defn view
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
  ;  :marker (map)(opt)
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
  ;
  ; @usage
  ; To associate a header with an input, use the same ID for both.
  ; [:<> [header     :my-field {...}]
  ;      [text-field :my-field {...}]]
  ([header-props]
   [view (random/generate-keyword) header-props])

  ([header-id header-props]
   ; @note (tutorials#parameterizing)
   (fn [_ header-props]
       (let [header-props (pretty-presets.engine/apply-preset           header-id header-props)
             header-props (header.prototypes/header-props-prototype     header-id header-props)
             header-props (pretty-inputs.engine/import-input-error-text header-id header-props)]
            [view-lifecycles header-id header-props]))))
