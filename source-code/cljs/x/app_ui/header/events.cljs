
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

(defn set-parent-route!
  ; @param (string) parent-route
  ;
  ; @usage
  ;  (r ui/set-parent-route! "/my-route")
  ;
  ; @usage
  ;  (r ui/set-parent-route! "/@app-home")
  ;
  ; @return (map)
  [db [_ parent-route]]
  (assoc-in db [:ui :header/meta-items :parent-route] parent-route))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @usage
;  [:ui/set-header-title! "My title"]
(a/reg-event-db :ui/set-header-title! set-header-title!)

; @usage
;  [:ui/remove-header-title!]
(a/reg-event-db :ui/remove-header-title! remove-header-title!)

; @usage
;  [:ui/set-parent-route! "/my-route"]
;
; @usage
;  [:ui/set-parent-route! "/@app-home"]
(a/reg-event-db :ui/set-parent-route! set-parent-route!)
