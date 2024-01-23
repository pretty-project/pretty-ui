
(ns pretty-inputs.combo-box.views
    (:require [fruits.hiccup.api                  :as hiccup]
              [fruits.loop.api                    :refer [reduce-indexed]]
              [fruits.random.api                  :as random]
              [metamorphic-content.api            :as metamorphic-content]
              [pretty-inputs.combo-box.attributes :as combo-box.attributes]
              [pretty-inputs.combo-box.env        :as combo-box.env]
              [pretty-inputs.combo-box.prototypes :as combo-box.prototypes]
              [pretty-inputs.text-field.views     :as text-field.views]
              [re-frame.api                       :as r]
              [reagent.api                        :as reagent]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- combo-box-option
  ; @ignore
  ;
  ; @param (keyword) box-id
  ; @param (map) box-props
  ; {:option-component (Reagent component symbol)(opt)
  ;  :option-label-f (function)}
  ; @param (integer) option-dex
  ; @param (map) option
  [box-id {:keys [option-component option-label-f] :as box-props} option-dex option]
  ; If no 'option-component' is passed, it displays the option in a DIV with the default ':pi-combo-box--option' class.
  [:button (combo-box.attributes/combo-box-option-attributes box-id box-props option-dex option)
           (if option-component [option-component box-id box-props option]
                                [:div {:class :pi-combo-box--option-label}
                                      (-> option option-label-f metamorphic-content/compose)])])

(defn- combo-box-options
  ; @ignore
  ;
  ; @param (keyword) box-id
  ; @param (map) box-props
  [box-id box-props]
  (let [options (combo-box.env/get-rendered-options box-id box-props)]
       (letfn [(f0 [option-dex option] [combo-box-option box-id box-props option-dex option])]
              [:div (combo-box.attributes/combo-box-options-attributes box-id box-props)
                    (hiccup/put-with-indexed [:<>] options f0)])))

(defn- combo-box-surface-content
  ; @ignore
  ;
  ; @param (keyword) box-id
  ; @param (map) box-props
  [box-id box-props]
  [:div (combo-box.attributes/combo-box-surface-content-attributes box-id box-props)
        [combo-box-options box-id box-props]])

(defn- combo-box
  ; @ignore
  ;
  ; @param (keyword) box-id
  ; @param (map) box-props
  [box-id box-props]
  (let [box-props (assoc-in box-props [:surface :content] [combo-box-surface-content box-id box-props])]
       [text-field.views/input box-id box-props]))

(defn input
  ; @note
  ; For more information, check out the documentation of the ['text-field'](#text-field) input.
  ;
  ; @param (keyword)(opt) box-id
  ; @param (map) box-props
  ; {:field-content-f (function)(opt)
  ;   Default: return
  ;  :field-value-f (function)(opt)
  ;   Default: return
  ;  :initial-options (vector)(opt)
  ;  :max-selection (integer)(opt)
  ;  :on-select (Re-Frame metamorphic-event)(opt)
  ;  :option-component (Reagent component symbol)(opt)
  ;   Default: pretty-inputs.combo-box.views/default-option-component
  ;  :option-label-f (function)(opt)
  ;   Default: return
  ;  :option-value-f (function)(opt)
  ;   Default: return
  ;  :options (vector)(opt)
  ;  :options-path (Re-Frame path vector)(opt)}
  ;
  ; @usage
  ; [combo-box {...}]
  ;
  ; @usage
  ; [combo-box :my-combo-box {...}]
  ([box-props]
   [input (random/generate-keyword) box-props])

  ([box-id box-props]
   ; @note (tutorials#parametering)
   (fn [_ box-props]
       (let [box-props (combo-box.prototypes/box-props-prototype box-id box-props)]
            [combo-box box-id box-props]))))
