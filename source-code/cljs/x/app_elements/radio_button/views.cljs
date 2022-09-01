
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-elements.radio-button.views
    (:require [reagent.api                            :as reagent]
              [x.app-components.api                   :as components]
              [x.app-core.api                         :as a]
              [x.app-elements.engine.api              :as engine]
              [x.app-elements.radio-button.helpers    :as radio-button.helpers]
              [x.app-elements.radio-button.prototypes :as radio-button.prototypes]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- radio-button-option
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) button-id
  ; @param (map) button-props
  ;  {:get-label-f (function)}
  ; @param (*) option
  [button-id {:keys [get-label-f] :as button-props} option]
  (let [option-label (get-label-f option)]
       [:button.x-radio-button--option (radio-button.helpers/radio-button-option-attributes button-id button-props option)
                                       [:div.x-radio-button--option-button]
                                       [:div.x-radio-button--option-label (components/content option-label)]]))

(defn- radio-button-options
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) button-id
  ; @param (map) button-props
  [button-id button-props]
  (let [options (engine/input-options button-id button-props)]
       (letfn [(f [option-list option] (conj option-list [radio-button-option button-id button-props option]))]
              (reduce f [:div.x-radio-button--options] options))))

(defn- radio-button-unselect-button
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) button-id
  ; @param (map) button-props
  ;  {:unselectable? (boolean)(opt)}
  [button-id {:keys [unselectable?] :as button-props}]
  (if unselectable? [:button.x-radio-button--clear-button (radio-button.helpers/clear-button-attributes button-id button-props)]))
                                                         ;[:div.x-radio-button--clear-button-label (components/content :delete!)]

(defn- radio-button
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) button-id
  ; @param (map) button-props
  [button-id button-props]
  [:div.x-radio-button (radio-button.helpers/radio-button-attributes button-id button-props)
                       [engine/element-header                        button-id button-props]
                       [radio-button-unselect-button                 button-id button-props]
                       [radio-button-options                         button-id button-props]])

(defn- stated-radio-button
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) button-id
  ; @param (map) button-props
  [button-id button-props]
  (reagent/lifecycles {:component-did-mount (fn [] (a/dispatch [:elements.radio-button/init-radio-button! button-id button-props]))
                       :reagent-render      (fn [_ button-props] [radio-button button-id button-props])}))

(defn element
  ; @param (keyword) button-id
  ; @param (map) button-props
  ;  {:border-color (keyword or string)(opt)
  ;    :default, :muted, :primary, :secondary, :success, :warning
  ;    Default: :primary
  ;   :class (keyword or keywords in vector)(opt)
  ;   :default-value (*)(opt)
  ;   :disabled? (boolean)(opt)
  ;    Default: false
  ;   :font-size (keyword)(opt)
  ;    :xs, :s
  ;    Default: :s
  ;   :get-label-f (function)(opt)
  ;    Default: return
  ;   :get-value-f (function)(opt)
  ;    Default: return
  ;   :helper (metamorphic-content)(opt)
  ;    TODO ...
  ;   :indent (map)(opt)
  ;    {:bottom (keyword)(opt)
  ;      :xxs, :xs, :s, :m, :l, :xl, :xxl
  ;     :left (keyword)(opt)
  ;      :xxs, :xs, :s, :m, :l, :xl, :xxl
  ;     :right (keyword)(opt)
  ;      :xxs, :xs, :s, :m, :l, :xl, :xxl
  ;     :top (keyword)(opt)
  ;      :xxs, :xs, :s, :m, :l, :xl, :xxl}
  ;   :initial-options (vector)(opt)
  ;   :initial-value (*)(opt)
  ;   :label (metamorphic-content)
  ;   :on-select (metamorphic-event)(opt)
  ;   :options (vector)(opt)
  ;   :options-orientation (keyword)(opt)
  ;    :horizontal, :vertical
  ;    Default: :vertical
  ;   :options-path (vector)(opt)
  ;   :required? (boolean or keyword)(opt)
  ;    true, false, :unmarked
  ;    Default: false
  ;   :style (map)(opt)
  ;   :unselectable? (boolean)(opt)
  ;    Default: false
  ;   :value-path (vector)(opt)}
  ;
  ; @usage
  ;  [elements/radio-button
  ;   {:options [{:value :foo :label "Foo"} {:value :bar :label "Bar"}]}]
  ;
  ; @usage
  ;  [elements/radio-button :my-radio-button {...}]
  ([button-props]
   [element (a/id) button-props])

  ([button-id {:keys [initial-options initial-value] :as button-props}]
   (let [button-props (radio-button.prototypes/button-props-prototype button-id button-props)]
        (if (or initial-options initial-value)
            [stated-radio-button button-id button-props]
            [radio-button        button-id button-props]))))
