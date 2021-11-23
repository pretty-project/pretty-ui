
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2021.01.02
; Description:
; Version: v0.2.6



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns app-fruits.window
    (:require [mid-fruits.uri  :as uri]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-uri
  ; @return (string)
  []
  (.-href (.-location js/window)))

(defn get-protocol
  ; @return (string)
  []
  (.-protocol (.-location js/window)))

(defn get-hostname
  ; @return (string)
  []
  (.-hostname (.-location js/window)))

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
  (.-userAgent (.-navigator js/window)))

(defn get-language
  ; @return (string)
  []
  (.-language (.-navigator js/window)))

(defn browser-online?
  ; @return (boolean)
  []
  (boolean (.-onLine (.-navigator js/window))))

(defn browser-offline?
  ; @return (boolean)
  []
  (not (browser-online?)))
