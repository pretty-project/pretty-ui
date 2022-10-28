
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-ui.themes.subs
    (:require [re-frame.api   :refer [r]]
              [x.app-user.api :as x.user]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-selected-theme
  ; @return (keyword)
  [db _]
  (r x.user/get-user-settings-item db :selected-theme))
