
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-ui.header.subs
    (:require [x.app-core.api          :as a :refer [r]]
              [x.app-ui.interface.subs :as interface.subs]))



;; ----------------------------------------------------------------------------
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
  (r interface.subs/application-interface? db))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; WARNING! NON-PUBLIC! DO NOT USE!
(a/reg-sub :ui/get-header-title get-header-title)

; WARNING! NON-PUBLIC! DO NOT USE!
(a/reg-sub :ui/render-header? render-header?)
