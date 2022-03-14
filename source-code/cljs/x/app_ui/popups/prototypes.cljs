
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-ui.popups.prototypes
    (:require [mid-fruits.candy     :refer [param]]
              [x.app-core.api       :refer [r]]
              [x.app-ui.popups.subs :as popups.subs]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn popup-props-prototype
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) popup-id
  ; @param (map) popup-props
  ;
  ; @return (map)
  ;  {:autopadding? (boolean)
  ;   :hide-animated? (boolean)
  ;   :horizontal-align (keyword)
  ;   :layout (keyword)
  ;   :min-width (keyword)
  ;   :reveal-animated? (boolean)
  ;   :update-animated? (boolean)
  ;   :user-close? (boolean)}
  [db [_ popup-id popup-props]]
  (merge {:autopadding?     true
          :hide-animated?   true
          :horizontal-align :center
          :layout           :boxed
          :min-width        :m
          :reveal-animated? true
          :update-animated? false
          :user-close?      true}
         (param popup-props)
         (if (r popups.subs/flip-layout-anyway? db popup-id popup-props)
             {:layout :flip})
         (if ; DEBUG
             (r a/debug-mode-detected? db)
             {:minimizable? true})))
