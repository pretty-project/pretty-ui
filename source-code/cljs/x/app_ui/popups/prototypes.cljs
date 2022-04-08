
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-ui.popups.prototypes
    (:require [mid-fruits.candy      :refer [param]]
              [x.app-core.api        :as a :refer [r]]
              [x.app-environment.api :as environment]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn flip-layout-anyway?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) popup-id
  ; @param (map) popup-props
  ;  {:layout (keyword)(opt)}
  ;
  ; @return (boolean)
  [db [_ _ {:keys [layout]}]]
  (and (r environment/viewport-small? db)
       (not= layout :unboxed)))

(defn popup-props-prototype
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) popup-id
  ; @param (map) popup-props
  ;
  ; @return (map)
  ;  {:hide-animated? (boolean)
  ;   :horizontal-align (keyword)
  ;   :layout (keyword)
  ;   :min-width (keyword)
  ;   :reveal-animated? (boolean)
  ;   :update-animated? (boolean)
  ;   :user-close? (boolean)}
  [db [_ popup-id popup-props]]
  (merge {:hide-animated?   true
          :horizontal-align :center
          :layout           :boxed
          :min-width        :m
          :reveal-animated? true
          :update-animated? false
          :user-close?      true}
         (param popup-props)
         (if (r flip-layout-anyway? db popup-id popup-props)
             {:layout :flip})
         (if ; DEBUG
             (r a/debug-mode-detected? db)
             {:minimizable? true})))
