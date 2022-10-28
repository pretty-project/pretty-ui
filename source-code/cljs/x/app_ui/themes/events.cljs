
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-ui.themes.events
    (:require [re-frame.api   :refer [r]]
              [x.app-user.api :as x.user]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn store-selected-theme!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) theme-id
  ;
  ; @return (map)
  [db [_ theme-id]]
  (r x.user/set-user-settings-item! db :selected-theme theme-id))
