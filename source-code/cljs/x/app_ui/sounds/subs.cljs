
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-ui.sounds.subs
    (:require [re-frame.api   :refer [r]]
              [x.app-user.api :as x.user]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn enabled-by-user?
  ; @return (boolean)
  [db _]
  (r x.user/get-user-profile-item db :notifications :notification-sounds.enabled?))
