
(ns pretty-inputs.color-selector.views
    (:require [fruits.random.api                       :as random]
              [metamorphic-content.api                 :as metamorphic-content]
              [pretty-elements.api                     :as pretty-elements]
              [pretty-inputs.color-selector.attributes :as color-selector.attributes]
              [pretty-inputs.color-selector.prototypes :as color-selector.prototypes]
              [pretty-inputs.core.side-effects :as core.side-effects]
              [pretty-inputs.input.env                 :as input.env]
              [pretty-layouts.api                      :as pretty-layouts]
              [pretty-presets.api                      :as pretty-presets]
              [reagent.api                             :as reagent]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- color-selector-option
  ; @ignore
  ;
  ; @param (keyword) selector-id
  ; @param (map) selector-props
  ; @param (string) option
  [selector-id selector-props option]
  [:button (color-selector.attributes/color-selector-option-attributes selector-id selector-props option)
           [:div {:class :pi-color-selector--option--color
                  :style {:background-color option}}]])

(defn- color-selector-option-list
  ; @ignore
  ;
  ; @param (keyword) selector-id
  ; @param (map) selector-props
  ; {:options (vector)}
  [selector-id {:keys [options] :as selector-props}]
  [:div (color-selector.attributes/color-selector-body-attributes selector-id selector-props)
        (letfn [(f0 [option-list option] (conj option-list [color-selector-option selector-id selector-props option]))]
               (reduce f0 [:<>] options))])

(defn- color-selector-popup-body
  ; @ignore
  ;
  ; @param (keyword) selector-id
  ; @param (map) selector-props
  [selector-id selector-props]
  ; @note (tutorials#parametering)
  (reagent/lifecycles {:component-did-mount (fn [_ _]); (r/dispatch [:pretty-inputs.color-selector/options-did-mount selector-id selector-props]))
                       :reagent-render      (fn [_ selector-props] [color-selector-option-list selector-id selector-props])}))

(defn- color-selector-popup-header
  ; @ignore
  ;
  ; @param (keyword) selector-id
  ; @param (map) selector-props
  ; {:popup (map)(opt)
  ;   {:label (metamorphic-content)(opt)}}
  [selector-id {{:keys [label]} :popup :as selector-props}]
  [:div {:class :pi-color-selector--popup--header :data-text-selectable false}
        (if label [:div (color-selector.attributes/color-selector-options-label-attributes selector-id selector-props)
                        (metamorphic-content/compose label)])])

(defn color-selector-popup
  ; @ignore
  ;
  ; @param (keyword) selector-id
  ; @param (map) selector-props
  ; {:popup (map)(opt)}
  [selector-id {:keys [popup] :as selector-props}]
  (if (input.env/popup-rendered? selector-id)
      [:div {:class :pi-color-selector--popup}
            [pretty-layouts/struct-popup :pretty-inputs.color-selector/popup
                                         (assoc popup :body     [color-selector-popup-body   selector-id selector-props]
                                                      :header   [color-selector-popup-header selector-id selector-props]
                                                      :on-cover {:fx [:pretty-inputs.input/close-popup! selector-id selector-props]})]]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- color-selector
  ; @ignore
  ;
  ; @param (keyword) selector-id
  ; @param (map) selector-props
  [selector-id selector-props]
  [:<> (let [on-click {:fx [:pretty-inputs.input/render-popup! selector-id selector-props]}]
            [pretty-elements/button selector-id (assoc selector-props :on-click on-click)])
       [color-selector-option-list selector-id selector-props]])

(defn- color-selector-lifecycles
  ; @ignore
  ;
  ; @param (keyword) selector-id
  ; @param (map) selector-props
  [selector-id selector-props]
  ; @note (tutorials#parametering)
  (reagent/lifecycles {:component-did-mount    (fn [_ _] (core.side-effects/input-did-mount    selector-id selector-props))
                       :component-will-unmount (fn [_ _] (core.side-effects/input-will-unmount selector-id selector-props))
                       :reagent-render         (fn [_ selector-props] [color-selector selector-id selector-props])}))

(defn input
  ; @note
  ; For more information, check out the documentation of the ['button'](/pretty-ui/cljs/pretty-elements/api.html#button) element.
  ;
  ; @param (keyword)(opt) selector-id
  ; @param (map) selector-props
  ; {:get-options-f (function)(opt)
  ;  :get-value-f (function)(opt)
  ;  :helper (metamorphic-content)(opt)
  ;  :initial-value (*)(opt)
  ;  :label (metamorphic-content)(opt)
  ;  :on-empty-f (function)(opt)
  ;  :on-invalid-f (function)(opt)
  ;  :on-mount-f (function)(opt)
  ;  :on-selected-f (function)(opt)
  ;  :on-unmount-f (function)(opt)
  ;  :on-unselected-f (function)(opt)
  ;  :on-valid-f (function)(opt)
  ;  :option-helper-f (function)(opt)
  ;  :option-label-f (function)(opt)
  ;  :option-value-f (function)(opt)
  ;  :popup (map)(opt)
  ;  :projected-value (*)(opt)
  ;  :set-value-f (function)(opt)
  ;  :validate-when-change? (boolean)(opt)
  ;  :validate-when-leave? (boolean)(opt)
  ;  :validators (maps in vector)(opt)
  ;   [{:f (function)
  ;     :invalid-message (metamorphic-content)(opt)}]}
  ;
  ; @usage
  ; [color-selector {...}]
  ;
  ; @usage
  ; [color-selector :my-color-selector {...}]
  ([selector-props]
   [input (random/generate-keyword) selector-props])

  ([selector-id selector-props]
   ; @note (tutorials#parametering)
   (fn [_ selector-props]
       (let [selector-props (pretty-presets/apply-preset                        selector-props)
             selector-props (color-selector.prototypes/selector-props-prototype selector-props)]
            [color-selector-lifecycles selector-id selector-props]))))
