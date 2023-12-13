
(ns pretty-elements.color-selector.views
    (:require [fruits.random.api                         :as random]
              [metamorphic-content.api                   :as metamorphic-content]
              [pretty-elements.button.views              :as button.views]
              [pretty-elements.color-selector.attributes :as color-selector.attributes]
              [pretty-elements.color-selector.prototypes :as color-selector.prototypes]
              [pretty-elements.icon-button.views         :as icon-button.views]
              [pretty-elements.input.env                 :as input.env]
              [pretty-layouts.api                        :as pretty-layouts]
              [pretty-presets.api                        :as pretty-presets]
              [re-frame.api                              :as r]
              [reagent.api                               :as reagent]))

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
           [:div {:class :pe-color-selector--option--color
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
  ; XXX#0106 (README.md#parametering)
  (reagent/lifecycles {:component-did-mount (fn [_ _] (r/dispatch [:pretty-elements.color-selector/options-did-mount selector-id selector-props]))
                       :reagent-render      (fn [_ selector-props] [color-selector-option-list selector-id selector-props])}))

(defn- color-selector-options-header
  ; @ignore
  ;
  ; @param (keyword) selector-id
  ; @param (map) selector-props
  ; {:popup (map)(opt)
  ;   {:label (metamorphic-content)(opt)}}
  [selector-id {{:keys [label]} :popup :as selector-props}]
  [:div {:class :pe-color-selector--options--header :data-selectable false}
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
      [:div {:class :pe-color-selector--options}
            [pretty-layouts/struct-popup :pretty-elements.color-selector/options
                                         (assoc popup :body     [color-selector-options-body   selector-id selector-props]
                                                      :header   [color-selector-options-header selector-id selector-props]
                                                      :on-cover {:fx [:pretty-elements.input/close-popup! selector-id selector-props]})]]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- color-selector
  ; @ignore
  ;
  ; @param (keyword) selector-id
  ; @param (map) selector-props
  [selector-id selector-props]
  [:<> (let [on-click {:fx [:pretty-elements.input/render-popup! selector-id selector-props]}]
            [button.views/element selector-id (assoc selector-props :on-click on-click)])
       [color-selector-options selector-id selector-props]])

(defn element
  ; @info
  ; XXX#0714 (source-code/cljs/pretty_elements/button/views.cljs)
  ; The 'color-selector' element is based on the 'button' element.
  ; For more information, check out the documentation of the 'button' element.
  ;
  ; @info
  ; XXX#0709
  ; Some other items based on the 'color-selector' element and their documentations link here.
  ;
  ; @param (keyword)(opt) selector-id
  ; @param (map) selector-props
  ; {:class (keyword or keywords in vector)(opt)
  ;  :indent (map)(opt)
  ;   {:bottom (keyword)(opt)
  ;    :left (keyword)(opt)
  ;    :right (keyword)(opt)
  ;    :top (keyword)(opt)
  ;    :horizontal (keyword)(opt)
  ;    :vertical (keyword)(opt)
  ;     :xxs, :xs, :s, :m, :l, :xl, :xxl, :3xl, :4xl, :5xl}
  ;  :on-select (Re-Frame metamorphic-event)(opt)
  ;  :options (strings in vector)(opt)
  ;  :options-label (metamorphic-content)(opt)
  ;  :options-path (Re-Frame path vector)(opt)
  ;  :outdent (map)(opt)
  ;   Same as the :indent property.
  ;  :popup (map)(opt)
  ;   {:border-color (keyword or string)(opt)
  ;    :border-position (keyword)(opt)
  ;    :border-radius (map)(opt)
  ;    :border-width (keyword)(opt)
  ;    :cover-color (keyword or string)(opt)
  ;     Default: :black
  ;    :fill-color (keyword or string)(opt)
  ;     Default: :default
  ;    :indent (map)(opt)
  ;    :label (metamorphic-content)(opt)
  ;    :min-width (keyword)(opt)
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
   [element (random/generate-keyword) selector-props])

  ([selector-id selector-props]
   (fn [_ selector-props] ; XXX#0106 (README.md#parametering)
       (let [selector-props (pretty-presets/apply-preset                                    selector-props)
             selector-props (color-selector.prototypes/selector-props-prototype selector-id selector-props)]
            [color-selector selector-id selector-props]))))
