
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.user.profile-handler.events)



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn set-user-profile-item!
  ; @param (keyword) item-key
  ; @param (*) item-value
  ;
  ; @usage
  ;  (r set-user-profile-item! db :last-name "Roger")
  ;
  ; @return (map)
  [db [_ item-key item-value]]
  (assoc-in db [:x.user :profile-handler/user-profile item-key] item-value))
