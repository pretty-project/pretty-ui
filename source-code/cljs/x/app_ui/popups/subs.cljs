
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-ui.popups.subs
    (:require [x.app-core.api        :as a :refer [r]]
              [x.app-environment.api :as environment]
              [x.app-ui.renderer     :as renderer]))



;; -- Subscriptions -----------------------------------------------------------
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

(defn render-touch-anchor?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) popup-id
  ;
  ; @return (boolean)
  [db [_ popup-id]]
  (let [layout (r renderer/get-element-prop db :popups popup-id :layout)]
       (and (= layout :boxed)
            (r environment/viewport-small?            db)
            (r environment/touch-events-api-detected? db))))

(defn get-popup-header-props
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) popup-id
  ;
  ; @return (map)
  ;  {:render-touch-anchor? (boolean)}
  [db [_ popup-id]]
  {:render-touch-anchor? false})
  ;:render-touch-anchor? (r geometry/render-touch-anchor? db popup-id)



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; WARNING! NON-PUBLIC! DO NOT USE!
(a/reg-sub :ui/get-popup-header-props get-popup-header-props)
