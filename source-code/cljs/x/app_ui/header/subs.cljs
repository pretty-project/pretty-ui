
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-ui.header.subs
    (:require [x.app-core.api     :as a :refer [r]]
              [x.app-db.api       :as db]
              [x.app-ui.interface :as interface]))



;; -- Subscriptions -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-header-title
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @return (metamorphic-content)
  [db _]
  (get-in db [:ui :header/meta-items :header-title]))

(defn render-header?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @return (boolean)
  [db _]
  (r interface/application-interface? db))

(defn get-route-parent
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @return (string)
  [db _]
  (get-in db [:ui :header/meta-items :route-parent]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; WARNING! NON-PUBLIC! DO NOT USE!
(a/reg-sub :ui/get-header-title get-header-title)

; WARNING! NON-PUBLIC! DO NOT USE!
(a/reg-sub :ui/render-header? render-header?)

; WARNING! NON-PUBLIC! DO NOT USE!
(a/reg-sub :ui/get-route-parent get-route-parent)
