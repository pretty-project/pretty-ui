
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.server-views.view-handler.subs
    (:require [x.mid-views.view-handler.subs :as view-handler.subs]
              [x.server-core.api             :as a :refer [r]]))



;; -- Redirects ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

; x.mid-views.view-handler.subs
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
;  [:views/get-views]
(a/reg-sub :views/get-view-screens get-view-screens)
