
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.server-ui.api
    (:require [x.server-ui.body.engine]
              [x.server-ui.head.engine]
              [x.server-ui.body.views   :as body.views]
              [x.server-ui.engine       :as engine]
              [x.server-ui.head.views   :as head.views]
              [x.server-ui.html.views   :as html.views]
              [x.server-ui.shield.views :as shield.views]))



;; -- Redirects ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

; x.server-ui.body.views
(def body body.views/view)

; x.server-ui.engine
(def include-js      engine/include-js)
(def include-css     engine/include-css)
(def include-favicon engine/include-favicon)
(def include-font    engine/include-font)

; x.server-ui.head.views
(def head head.views/view)

; x.server-ui.html.views
(def html html.views/view)

; x.server-ui.shield.views
(def app-shield shield.views/view)
