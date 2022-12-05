
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript web application framework
; https://monotech-hq.github.io/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns engines.file-editor.routes.subs
    (:require [engines.engine-handler.routes.subs :as routes.subs]
              [engines.file-editor.transfer.subs  :as transfer.subs]
              [re-frame.api                       :as r :refer [r]]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; engines.engine-handler.routes.subs
(def route-handled? routes.subs/route-handled?)
