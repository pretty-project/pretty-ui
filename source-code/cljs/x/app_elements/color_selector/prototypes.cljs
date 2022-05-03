
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-elements.color-selector.prototypes
    (:require [mid-fruits.candy                     :refer [param return]]
              [mid-fruits.logical                   :refer [nor]]
              [x.app-elements.color-selector.config :as color-selector.config]
              [x.app-elements.engine.api            :as engine]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn options-props-prototype
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) selector-id
  ; @param (map) selector-props
  ;  {:options (strings in vector)(opt)
  ;   :options-path (vector)(opt)}
  ;
  ; @return (map)
  ;  {:options (strings in vector)}
  [db [_ _ {:keys [options-path] :as selector-props}]]
  (if options-path (assoc  selector-props :options (get-in db options-path))
                   (return selector-props)))

(defn selector-props-prototype
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) selector-id
  ; @param (map) selector-props
  ;  {:options (strings in vector)(opt)
  ;   :options-path (vector)(opt)}
  ;
  ; @return (map)
  ;  {:no-colors-label (metamorphic-content)
  ;   :size (keyword)
  ;   :value-path (vector)}
  [selector-id {:keys [options options-path] :as selector-props}]
  (merge {:no-colors-label color-selector.config/DEFAULT-NO-COLORS-LABEL
          :size            :s
          :value-path      (engine/default-value-path selector-id)}
         (param selector-props)
         (if (nor options options-path) {:options color-selector.config/DEFAULT-OPTIONS})))
