
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-layouts.api
    (:require [x.app-layouts.form-a.helpers :as form-a.helpers]
              [x.app-layouts.form-a.views   :as form-a.views]
              [x.app-layouts.layout-a.views :as layout-a.views]))



;; -- Redirects ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

; x.app-layouts.form-a.helpers
(def form-block-attributes  form-a.helpers/form-block-attributes)
(def form-row-attributes    form-a.helpers/form-row-attributes)
(def form-column-attributes form-a.helpers/form-column-attributes)

; x.app-layouts.form-a.views
(def form-group-label  form-a.views/form-group-label)
(def form-group-header form-a.views/form-group-header)

; x.app-layouts.layout-a.views
(def layout-a layout-a.views/layout)
