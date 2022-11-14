
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.views.view-handler.subs
    (:require [mid.x.views.view-handler.subs :as view-handler.subs]
              [re-frame.api                  :as r :refer [r]]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; mid.x.views.view-handler.subs
(def get-error-screen view-handler.subs/get-error-screen)
(def get-login-screen view-handler.subs/get-login-screen)
(def get-menu-screen  view-handler.subs/get-menu-screen)



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- get-view-screens
  ; WARNING! NON PUBLIC! DO NOT USE!
  ;
  ; @return (map)
  ;  {:error-screen (metamorphic-event)
  ;   :login-screen (metamorphic-event)
  ;   :menu-screen (metamorphic-event)}
  [db _]
  {:error-screen (r get-error-screen db)
   :login-screen (r get-login-screen db)
   :menu-screen  (r get-menu-screen  db)})



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; WARNING! NON-PUBLIC! DO NOT USE!
;
; @usage
;  [:x.views/get-views]
(r/reg-sub :x.views/get-view-screens get-view-screens)
