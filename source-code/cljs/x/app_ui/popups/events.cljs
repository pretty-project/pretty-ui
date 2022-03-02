
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-ui.popups.events
    (:require [x.app-core.api    :as a :refer [r]]
              [x.app-ui.renderer :as renderer]))



;; -- DB events ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn minimize-popup!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) popup-id
  ;
  ; @return (map)
  [db [_ popup-id]]
  (r renderer/set-element-prop! db :popups popup-id :minimized? true))

(defn maximize-popup!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) popup-id
  ;
  ; @return (map)
  [db [_ popup-id]]
  (r renderer/set-element-prop! db :popups popup-id :minimized? false))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; WARNING! NON-PUBLIC! DO NOT USE!
(a/reg-event-db :ui/minimize-popup! minimize-popup!)

; WARNING! NON-PUBLIC! DO NOT USE!
(a/reg-event-db :ui/maximize-popup! maximize-popup!)
