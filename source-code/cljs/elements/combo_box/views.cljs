
(ns elements.combo-box.views
    (:require [elements.combo-box.helpers    :as combo-box.helpers]
              [elements.combo-box.attributes :as combo-box.attributes]
              [elements.combo-box.prototypes :as combo-box.prototypes]
              [elements.text-field.helpers   :as text-field.helpers]
              [elements.text-field.views     :as text-field.views]
              [hiccup.api                    :as hiccup]
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
  ; If no option component passed, displaying the option with the default :e-combo-box--option class
  [:button (combo-box.attributes/combo-box-option-attributes box-id box-props option-dex option)
           (if option-component [option-component box-id box-props option]
                                [:div {:class :e-combo-box--option-label}
                                      (-> option option-label-f x.components/content)])])

(defn- combo-box-options
  ; @ignore
  ;
  ; @param (keyword) box-id
  ; @param (map) box-props
  [box-id box-props]
  (let [options (combo-box.helpers/get-rendered-options box-id box-props)]
       (letfn [(f [option-dex option] [combo-box-option box-id box-props option-dex option])]
              [:div (combo-box.attributes/combo-box-options-attributes box-id box-props)
                    (hiccup/put-with-indexed [:<>] options f)])))

(defn- combo-box-surface
  ; @ignore
  ;
  ; @param (keyword) box-id
  ; @param (map) box-props
  [box-id box-props]
  [:div (combo-box.attributes/combo-box-surface-attributes box-id box-props)
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
