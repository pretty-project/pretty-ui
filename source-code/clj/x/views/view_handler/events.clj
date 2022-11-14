
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.views.view-handler.events)



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn set-error-screen!
  ; WARNING! NON PUBLIC! DO NOT USE!
  ;
  ; @param (metamorphic-event) error-screen
  [db [_ error-screen]]
  (assoc-in db [:x.views :view-handler/meta-items :error-screen] error-screen))

(defn set-login-screen!
  ; WARNING! NON PUBLIC! DO NOT USE!
  ;
  ; @param (metamorphic-event) login-screen
  [db [_ login-screen]]
  (assoc-in db [:x.views :view-handler/meta-items :login-screen] login-screen))

(defn set-menu-screen!
  ; WARNING! NON PUBLIC! DO NOT USE!
  ;
  ; @param (metamorphic-event) menu-screen
  [db [_ menu-screen]]
  (assoc-in db [:x.views :view-handler/meta-items :menu-screen] menu-screen))
