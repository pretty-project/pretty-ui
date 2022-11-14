
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.locales.language-handler.events
    (:require [re-frame.api :as r :refer [r]]
              [x.user.api   :as x.user]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn select-language!
  ; @param (keyword) language-id
  ;
  ; @usage
  ;  (r get-selected-language db :en)
  ;
  ; @return (map)
  [db [_ language-id]]
  (r x.user/set-user-settings-item! db :selected-language language-id))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @usage
;  [:x.locales/select-language! :en]
(r/reg-event-db :x.locales/select-language! select-language!)
