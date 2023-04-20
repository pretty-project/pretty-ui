
(ns elements.color-selector.views
    (:require [elements.button.views              :as button.views]
              [elements.color-selector.attributes :as color-selector.attributes]
              [elements.color-selector.prototypes :as color-selector.prototypes]
              [elements.icon-button.views         :as icon-button.views]
              [elements.input.env                 :as input.env]
              [layouts.api                        :as layouts]
              [metamorphic-content.api            :as metamorphic-content]
              [random.api                         :as random]
              [re-frame.api                       :as r]
              [reagent.api                        :as reagent]))

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
           [:div {:class :e-color-selector--option--color
                  :style {:background-color option}}]])

(defn- color-selector-option-list
  ; @ignore
  ;
  ; @param (keyword) selector-id
  ; @param (map) selector-props
  ; {:options (vector)}
  [selector-id {:keys [options] :as selector-props}]
  [:div (color-selector.attributes/color-selector-body-attributes selector-id selector-props)
        (letfn [(f [option-list option] (conj option-list [color-selector-option selector-id selector-props option]))]
               (reduce f [:<>] options))])

(defn- color-selector-options-body
  ; @ignore
  ;
  ; @param (keyword) selector-id
  ; @param (map) selector-props
  [selector-id selector-props]
  (reagent/lifecycles {:component-did-mount (fn [_ _] (r/dispatch [:elements.color-selector/options-did-mount selector-id selector-props]))
                       :reagent-render      (fn [_ _] [color-selector-option-list selector-id selector-props])}))

(defn- color-selector-options-header
  ; @ignore
  ;
  ; @param (keyword) selector-id
  ; @param (map) selector-props
  ; {:popup (map)(opt)
  ;   {:label (metamorphic-content)(opt)}}
  [selector-id {{:keys [label]} :popup :as selector-props}]
  [:div {:class :e-color-selector--options--header :data-selectable false}
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
      [:div {:class :e-color-selector--options}
            [layouts/struct-popup :elements.color-selector/options
                                  (assoc popup :body     [color-selector-options-body   selector-id selector-props]
                                               :header   [color-selector-options-header selector-id selector-props]
                                               :on-cover {:fx [:elements.input/close-popup! selector-id selector-props]})]]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- color-selector
  ; @ignore
  ;
  ; @param (keyword) selector-id
  ; @param (map) selector-props
  [selector-id selector-props]
  [:<> (let [on-click {:fx [:elements.input/render-popup! selector-id selector-props]}]
            [button.views/element selector-id (assoc selector-props :on-click on-click)])
       [color-selector-options selector-id selector-props]])

(defn element
  ; XXX#0714 (source-code/cljs/elements/button/views.cljs)
  ; The color-selector element is based on the button element.
  ; For more information check out the documentation of the button element.
  ;
  ; XXX#0709
  ; Some other items based on the color-selector element and their documentations link here.
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
  ;   Same as the :indent property
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
  ;    :outdent (map)(opt)}
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
   (let [selector-props (color-selector.prototypes/selector-props-prototype selector-id selector-props)]
        [color-selector selector-id selector-props])))
