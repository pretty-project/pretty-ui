
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-ui.header.events
    (:require [mid-fruits.map :refer [dissoc-in]]
              [x.app-core.api :as a]
              [x.app-db.api   :as db]))



;; -- DB events ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn set-header-title!
  ; @param (metamorphic-content) header-title
  ;
  ; @usage
  ;  (r ui/set-header-title! "My title")
  ;
  ; @return (map)
  [db [_ header-title]]
  (assoc-in db [:ui :header/meta-items :header-title] header-title))

(defn remove-header-title!
  ; @param (metamorphic-content) header-title
  ;
  ; @usage
  ;  (r ui/remove-header-title!)
  ;
  ; @return (map)
  [db [_ header-title]]
  (dissoc-in db [:ui :header/meta-items :header-title]))

(defn set-route-parent!
  ; @param (string) route-parent
  ;
  ; @usage
  ;  (r ui/set-route-parent! "/my-route")
  ;
  ; @usage
  ;  (r ui/set-route-parent! "/@app-home")
  ;
  ; @return (map)
  [db [_ route-parent]]
  (assoc-in db [:ui :header/meta-items :route-parent] route-parent))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @usage
;  [:ui/set-header-title! "My title"]
(a/reg-event-db :ui/set-header-title! set-header-title!)

; @usage
;  [:ui/remove-header-title!]
(a/reg-event-db :ui/remove-header-title! remove-header-title!)

; @usage
;  [:ui/set-route-parent! "/my-route"]
;
; @usage
;  [:ui/set-route-parent! "/@app-home"]
(a/reg-event-db :ui/set-route-parent! set-route-parent!)
