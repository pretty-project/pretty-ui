
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.server-locales.name-handler.helpers
    (:require [x.mid-locales.name-handler.helpers   :as name-handler.helpers]
              [x.server-user.api                    :as user]
              [x.server-locales.name-handler.config :as name-handler.config]))



;; -- Redirects ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

; x.mid-locales.name-handler.helpers
(def name->ordered-name name-handler.helpers/name->ordered-name)



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn request->name-order
  ; @param (map) request
  ;
  ; @return (keyword)
  ;  :normal, :reversed
  [request]
  (let [selected-language (user/request->user-settings-item request :selected-language)]
       (get name-handler.config/NAME-ORDERS selected-language)))
