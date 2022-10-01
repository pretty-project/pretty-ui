
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns plugins.file-editor.routes.subs
    (:require [plugins.file-editor.transfer.subs  :as transfer.subs]
              [plugins.plugin-handler.routes.subs :as routes.subs]
              [re-frame.api                       :as r :refer [r]]))



;; -- Redirects ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

; plugins.plugin-handler.routes.subs
(def route-handled? routes.subs/route-handled?)
