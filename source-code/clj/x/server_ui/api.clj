
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.server-ui.api
    (:require [x.server-ui.body.views   :as body.views]
              [x.server-ui.core.helpers :as core.helpers]
              [x.server-ui.head.views   :as head.views]
              [x.server-ui.html.views   :as html.views]
              [x.server-ui.shield.views :as shield.views]))



;; -- Redirects ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

; x.server-ui.body.views
(def body body.views/view)

; x.server-ui.core.helpers
(def include-js      core.helpers/include-js)
(def include-css     core.helpers/include-css)
(def include-favicon core.helpers/include-favicon)
(def include-font    core.helpers/include-font)

; x.server-ui.head.views
(def head head.views/view)

; x.server-ui.html.views
(def html html.views/view)

; x.server-ui.shield.views
(def app-shield shield.views/view)
