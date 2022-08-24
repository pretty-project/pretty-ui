
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.mid-views.view-handler.subs)



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-error-screen
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @return (metamorphic-event)
  [db _]
  (get-in db [:views :view-handler/meta-items :error-screen]))

(defn get-login-screen
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @return (metamorphic-event)
  [db _]
  (get-in db [:views :view-handler/meta-items :login-screen]))

(defn get-menu-screen
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @return (metamorphic-event)
  [db _]
  (get-in db [:views :view-handler/meta-items :menu-screen]))
