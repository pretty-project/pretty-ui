
(ns pretty-inputs.color-selector.views
    (:require [fruits.random.api                       :as random]
              [metamorphic-content.api                 :as metamorphic-content]
              [pretty-elements.api                     :as pretty-elements]
              [pretty-inputs.color-selector.attributes :as color-selector.attributes]
              [pretty-inputs.color-selector.prototypes :as color-selector.prototypes]
              [pretty-inputs.input.env                 :as input.env]
              [pretty-layouts.api                      :as pretty-layouts]
              [pretty-presets.api                      :as pretty-presets]
              [re-frame.api                            :as r]
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

(defn- color-selector-options-body
  ; @ignore
  ;
  ; @param (keyword) selector-id
  ; @param (map) selector-props
  [selector-id selector-props]
  ; @note (tutorials#parametering)
  (reagent/lifecycles {:component-did-mount (fn [_ _] (r/dispatch [:pretty-inputs.color-selector/options-did-mount selector-id selector-props]))
                       :reagent-render      (fn [_ selector-props] [color-selector-option-list selector-id selector-props])}))

(defn- color-selector-options-header
  ; @ignore
  ;
  ; @param (keyword) selector-id
  ; @param (map) selector-props
  ; {:popup (map)(opt)
  ;   {:label (metamorphic-content)(opt)}}
  [selector-id {{:keys [label]} :popup :as selector-props}]
  [:div {:class :pi-color-selector--options--header :data-text-selectable false}
        (if label [:div (color-selector.attributes/color-selector-options-label-attributes selector-id selector-props)
                        (metamorphic-content/compose label)])])

(defn color-selector-options
  ; @ignore
  ;
  ; @param (keyword) selector-id
  ; @param (map) selector-props
  ; {:popup (map)(opt)}
  [selector-id {:keys [popup] :as selector-props}]
  (if (input.env/popup-rendered? selector-id)
      [:div {:class :pi-color-selector--options}
            [pretty-layouts/struct-popup :pretty-inputs.color-selector/options
                                         (assoc popup :body     [color-selector-options-body   selector-id selector-props]
                                                      :header   [color-selector-options-header selector-id selector-props]
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
       [color-selector-options selector-id selector-props]])

(defn input
  ; @note
  ; For more information, check out the documentation of the ['button'](/pretty-ui/cljs/pretty-elements/api.html#button) element.
  ;
  ; @param (keyword)(opt) selector-id
  ; @param (map) selector-props
  ; {:class (keyword or keywords in vector)(opt)
  ;  :indent (map)(opt)
  ;   {:all, :bottom, :left, :right, :top, :horizontal, :vertical (keyword, px or string)(opt)}
  ;  :on-select (Re-Frame metamorphic-event)(opt)
  ;  :options (strings in vector)(opt)
  ;  :options-label (metamorphic-content)(opt)
  ;  :options-path (Re-Frame path vector)(opt)
  ;  :outdent (map)(opt)
  ;   {:all, :bottom, :left, :right, :top, :horizontal, :vertical (keyword, px or string)(opt)}
  ;  :popup (map)(opt)
  ;   {:border-color (keyword or string)(opt)
  ;    :border-position (keyword)(opt)
  ;    :border-radius (map)(opt)
  ;     {:all, :tl, :tr, :br, :bl (keyword, px or string)(opt)}
  ;    :border-width (keyword, px or string)(opt)
  ;    :click-effect (keyword)(opt)
  ;     Default: :opacity
  ;    :cover-color (keyword or string)(opt)
  ;     Default: :black
  ;    :fill-color (keyword or string)(opt)
  ;     Default: :default
  ;    :fill-pattern (keyword)(opt)
  ;     Default: :cover
  ;    :hover-effect (keyword)(opt)
  ;    :indent (map)(opt)
  ;    :label (metamorphic-content)(opt)
  ;    :min-width (keyword, px or string)(opt)
  ;    :outdent (map)(opt)
  ;    :preset (keyword)(opt)}
  ;  :preset (keyword)(opt)
  ;  :style (map)(opt)
  ;  :value-path (Re-Frame path vector)(opt)}
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
       (let [selector-props (pretty-presets/apply-preset                                    selector-props)
             selector-props (color-selector.prototypes/selector-props-prototype selector-id selector-props)]
            [color-selector selector-id selector-props]))))
