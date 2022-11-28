
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.core.config-handler.prototypes
    (:require [string.api :as string]
              [uri.api    :as uri]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn app-config-prototype
  ; @param (map) app-config
  ;
  ; @return (map)
  [app-config]
  ; XXX#6700
  ; A rendszer számára szükséges biztosítani, hogy az app-domain és app-home
  ; értéke érvényes domain és útvonal legyen!
  (-> app-config (update :app-domain uri/valid-domain)
                 (update :app-home   uri/valid-path)))

(defn server-config-prototype
  ; @param (map) server-config
  ;
  ; @return (map)
  [server-config]
  ; XXX#6701
  ; A rendszer számára szükséges biztosítani, hogy a portok és maximális
  ; fájl/tárhely méretek értéke integer típus legyen!
  (-> server-config (update :database-port    string/to-integer)
                    (update :default-port     string/to-integer)
                    (update :max-body         string/to-integer)
                    (update :max-upload-size  string/to-integer)
                    (update :storage-capacity string/to-integer)))
