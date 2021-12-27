
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2021.01.02
; Description:
; Version: v0.3.6



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns app-fruits.window
    (:require [mid-fruits.uri :as uri]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-uri
  ; @return (string)
  []
  (-> js/window .-location .-href))

(defn get-protocol
  ; @return (string)
  []
  (-> js/window .-location .-protocol))

(defn get-hostname
  ; @return (string)
  []
  (-> js/window .-location .-hostname))

(defn get-uri-base
  ; @example
  ;  (window/get-uri-base)
  ;  => "https://my-domain.com"
  ;
  ; @return (string)
  []
  (let [protocol (get-protocol)
        hostname (get-hostname)]
       (str protocol "//" hostname)))

(defn get-user-agent
  ; @return (string)
  []
  (-> js/window .-navigator .-userAgent))

(defn get-language
  ; @return (string)
  []
  (-> js/window .-navigator .-language))

(defn browser-online?
  ; @return (boolean)
  []
  (-> js/window .-navigator .-onLine boolean))

(defn browser-offline?
  ; @return (boolean)
  []
  (-> js/window .-navigator .-onLine not))
