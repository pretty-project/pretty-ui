
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.ui.api
    (:require [x.ui.body.views   :as body.views]
              [x.ui.core.helpers :as core.helpers]
              [x.ui.head.views   :as head.views]
              [x.ui.html.views   :as html.views]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; x.ui.body.views
(def body body.views/view)

; x.ui.core.helpers
(def include-js      core.helpers/include-js)
(def include-css     core.helpers/include-css)
(def include-favicon core.helpers/include-favicon)
(def include-font    core.helpers/include-font)

; x.ui.head.views
(def head head.views/view)

; x.ui.html.views
(def html html.views/view)
