
(ns elements.combo-box.views
    (:require [elements.combo-box.helpers    :as combo-box.helpers]
              [elements.combo-box.prototypes :as combo-box.prototypes]
              [elements.text-field.helpers   :as text-field.helpers]
              [elements.text-field.views     :as text-field.views]
              [loop.api                      :refer [reduce-indexed]]
              [random.api                    :as random]
              [re-frame.api                  :as r]
              [reagent.api                   :as reagent]
              [x.components.api              :as x.components]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- combo-box-option
  ; @ignore
  ;
  ; @param (keyword) box-id
  ; @param (map) box-props
  ; {:option-component (component)(opt)
  ;  :option-label-f (function)}
  ; @param (integer) option-dex
  ; @param (map) option
  [box-id {:keys [option-component option-label-f] :as box-props} option-dex option]
  ; BUG#2105 (source-code/cljs/elements/plain_field/helpers.cljs)
  [:button {:class :e-combo-box--option
            :on-mouse-down #(.preventDefault %)
            :on-mouse-up   #(r/dispatch [:elements.combo-box/select-option! box-id box-props option])
            ;:data-selected ...
            :data-highlighted (= option-dex (combo-box.helpers/get-highlighted-option-dex box-id))}
           (if option-component [option-component box-id box-props option]

                                ; If no option component passed, displaying the option with the default :e-combo-box--option class
                                [:div {:class :e-combo-box--option-label} (-> option option-label-f x.components/content)])])

(defn- combo-box-options
  ; @ignore
  ;
  ; @param (keyword) box-id
  ; @param (map) box-props
  [box-id box-props]
  ; Why the :data-options-rendered attribute is added?
  ; HACK#1450 (source-code/cljs/elements/combo_box/helpers.cljs)
  (let [options (combo-box.helpers/get-rendered-options box-id box-props)]
       (letfn [(f [option-list option-dex option]
                  ;^{:key (random/generate-react-key)}
                  (conj option-list [combo-box-option box-id box-props option-dex option]))]
              [:div {:class :e-combo-box--options
                     :data-options-rendered (-> options empty? not)
                     :data-scroll-axis :y}
                    (reduce-indexed f [:<>] options)])))

(defn- combo-box-surface
  ; @ignore
  ;
  ; @param (keyword) box-id
  ; @param (map) box-props
  [box-id box-props]
  [:div {:class            :e-combo-box--surface
         :data-font-size   :s
         :data-line-height :text-block}
        [combo-box-options box-id box-props]])

(defn- combo-box-structure
  ; @ignore
  ;
  ; @param (keyword) box-id
  ; @param (map) box-props
  [box-id box-props]
  (let [box-props (assoc box-props :surface [combo-box-surface box-id box-props])]
       [text-field.views/element box-id box-props]))

(defn- combo-box
  ; @ignore
  ;
  ; @param (keyword) box-id
  ; @param (map) box-props
  [box-id box-props]
  (reagent/lifecycles {:component-did-mount (fn [_ _] (r/dispatch [:elements.combo-box/box-did-mount box-id box-props]))
                       :reagent-render      (fn [_ box-props] [combo-box-structure box-id box-props])}))

(defn element
  ; XXX#0714 (source-code/cljs/elements/text_field/views.cljs)
  ; The combo-box element is based on the text-field element.
  ; For more information check out the documentation of the text-field element.
  ;
  ; @param (keyword)(opt) box-id
  ; @param (map) box-props
  ; {:field-content-f (function)(opt)
  ;   Default: return
  ;  :field-value-f (function)(opt)
  ;   Default: return
  ;  :initial-options (vector)(opt)
  ;  :on-select (metamorphic-event)(opt)
  ;  :option-component (component)(opt)
  ;   Default: elements.combo-box.views/default-option-component
  ;  :option-label-f (function)(opt)
  ;   Default: return
  ;  :option-value-f (function)(opt)
  ;   Default: return
  ;  :options (vector)(opt)
  ;  :options-path (vector)(opt)}
  ;
  ; @usage
  ; [combo-box {...}]
  ;
  ; @usage
  ; [combo-box :my-combo-box {...}]
  ([box-props]
   [element (random/generate-keyword) box-props])

  ([box-id box-props]
   (let [box-props (combo-box.prototypes/box-props-prototype box-id box-props)]
        [combo-box box-id box-props])))
