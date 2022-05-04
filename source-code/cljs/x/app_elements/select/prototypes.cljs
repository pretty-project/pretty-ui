
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-elements.select.prototypes
    (:require [mid-fruits.candy                              :refer [param return]]
              [mid-fruits.vector                             :as vector]
              [x.app-components.api                          :as components]
              [x.app-core.api                                :as a :refer [r]]
              [x.app-elements.engine.api                     :as engine]
              [x.app-elements.element-components.button      :as button :rename {element button}]
              [x.app-elements.element-components.icon-button :as icon-button]
              [x.app-elements.element-components.label               :rename {element label}]
              [x.app-elements.element-components.horizontal-polarity :rename {element horizontal-polarity}]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- select-props-prototype
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) select-id
  ; @param (map) select-props
  ;  {:value-path (vector)}
  ;
  ; @return (map)
  ;  {:get-label-f (function)
  ;   :get-value-f (function)
  ;   :layout (keyword)
  ;   :no-options-label (metamorphic-content)
  ;   :value-path (vector)}
  [select-id select-props]
  (merge {:get-label-f      return
          :get-value-f      return
          :layout           :select
          :no-options-label :no-options
          :options-path     (engine/default-options-path select-id)
          :value-path       (engine/default-value-path   select-id)}
         (param select-props)))
         ;{:on-click [:elements/render-select-options! select-id options-props]}))

(defn- options-props-prototype
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) select-id
  ; @param (map) options-props
  ;
  ; @return (map)
  ;  {:get-label-f (function)
  ;   :no-options-label (metamorphic-content)
  ;   :options-path (vector)
  ;   :value-path (vector)}
  [select-id options-props]
  (merge {:get-label-f      return
          :get-value-f      return
          :no-options-label :no-options
          :options-path     (engine/default-options-path select-id)
          :value-path       (engine/default-value-path   select-id)}
         (param options-props)))
